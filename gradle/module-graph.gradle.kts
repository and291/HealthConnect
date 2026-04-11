import org.gradle.api.artifacts.ProjectDependency

// =============================================================================
// Module Graph Generators
//
//   ./gradlew generateInternalModuleGraph  →  INTERNAL_MODULE_GRAPH.md
//   ./gradlew generateExternalModuleGraph  →  EXTERNAL_MODULE_GRAPH.md
//
// Both tasks re-read the live Gradle model on every run, so they stay accurate
// as the project evolves. Commit the generated .md files so reviewers can see
// the graph on GitHub without running a local build.
// =============================================================================

// Configurations that carry real compile-time dependencies worth graphing.
// testImplementation / androidTestImplementation / debugImplementation are excluded.
val graphedConfigs = listOf("implementation", "api", "compileOnly")

// =============================================================================
// Data collection
// =============================================================================

/**
 * Returns {module path → [depended-upon module paths]} for all subprojects.
 * Only project-to-project dependencies are included.
 */
fun collectInternalDeps(): Map<String, List<String>> =
    subprojects.associate { sub ->
        val paths = mutableListOf<String>()
        graphedConfigs.forEach { cfgName ->
            sub.configurations.findByName(cfgName)?.dependencies?.forEach { dep ->
                if (dep is ProjectDependency) paths += dep.path
            }
        }
        sub.path to paths.distinct()
    }

/**
 * Returns {module path → ["group:name", ...]} for all subprojects.
 * Only direct external (non-project) dependencies are included.
 * BOMs are excluded — they influence versions, not the dependency graph.
 */
fun collectExternalDeps(): Map<String, List<String>> =
    subprojects.associate { sub ->
        val coords = mutableListOf<String>()
        graphedConfigs.forEach { cfgName ->
            sub.configurations.findByName(cfgName)?.dependencies?.forEach { dep ->
                if (dep !is ProjectDependency && dep.group != null && !dep.name.contains("bom")) {
                    coords += "${dep.group}:${dep.name}"
                }
            }
        }
        sub.path to coords.distinct()
    }

// =============================================================================
// Mermaid helpers
// =============================================================================

/** Stable Mermaid node ID — strips ":" prefix, replaces non-alphanumeric chars. */
fun String.toNodeId(): String =
    removePrefix(":").replace(Regex("[^a-zA-Z0-9]"), "_")

/** Short display label shown inside the node box. Strips ":app:" prefix. */
fun String.toLabel(): String =
    removePrefix(":app:").ifEmpty { "app" }

/**
 * Extracts the feature-group segment of a project path (e.g. "components" from
 * ":app:components:api"). Returns null for ":app" itself, which is not grouped.
 */
fun moduleGroup(path: String): String? =
    path.removePrefix(":").split(":").takeIf { it.size >= 3 }?.get(1)

/**
 * An edge from [from] to [to] is an architecture violation when [to] is an
 * :impl module and [from] is not :app.
 *
 * Rationale: :impl modules are internal implementation details. Only their
 * paired :api module may be shared across feature boundaries. Depending on an
 * :impl module from outside couples the consumer to internal details and
 * prevents independent evolution of the implementation.
 *
 * :app is the composition root and is exempt — it deliberately wires everything.
 */
fun isViolation(from: String, to: String): Boolean =
    from != ":app" && to.endsWith(":impl")

/** Stable Mermaid node ID for an external library coordinate ("group:name"). */
fun extNodeId(coord: String): String =
    "ext_" + coord.replace(Regex("[^a-zA-Z0-9]"), "_")

/** Short label shown inside the external dependency node — artifact name only. */
fun extLabel(coord: String): String = coord.substringAfter(":")

/** Assigns a display category to an external dependency for subgraph grouping. */
fun extCategory(coord: String): String {
    val g = coord.substringBefore(":")
    return when {
        g == "androidx.compose.material3.adaptive"       -> "Navigation"
        g.startsWith("androidx.compose")                 -> "Compose"
        g.startsWith("androidx.health")                  -> "Health Connect"
        g.startsWith("androidx.navigation3")             -> "Navigation"
        g.startsWith("androidx.lifecycle")               -> "Lifecycle"
        g == "androidx.core" || g == "androidx.activity" -> "Core"
        g.startsWith("org.jetbrains")                    -> "Kotlin"
        else                                             -> "Other"
    }
}

// =============================================================================
// Graph builders
// =============================================================================

fun buildInternalGraph(deps: Map<String, List<String>>): String {
    val validEdges = mutableListOf<Pair<String, String>>()
    val badEdges   = mutableListOf<Pair<String, String>>()
    deps.forEach { (from, tos) ->
        if (from == ":app") return@forEach
        tos.filter { it != ":app" }.forEach { to ->
            if (isViolation(from, to)) badEdges  += from to to
            else                       validEdges += from to to
        }
    }
    val allEdges = validEdges + badEdges  // valid first — linkStyle is index-based

    val allPaths = (deps.keys + deps.values.flatten()).distinct().sorted()
    val byGroup  = allPaths.groupBy { moduleGroup(it) }

    return buildString {
        appendLine("flowchart TD")
        appendLine()

        appendLine("    app([\":app\"])")
        appendLine()

        byGroup.filterKeys { it != null }.entries.sortedBy { it.key }.forEach { (group, paths) ->
            appendLine("    subgraph $group")
            paths.sorted().forEach { p -> appendLine("        ${p.toNodeId()}[\"${p.toLabel()}\"]") }
            appendLine("    end")
            appendLine()
        }

        validEdges.forEach { (f, t) -> appendLine("    ${f.toNodeId()} --> ${t.toNodeId()}") }

        if (badEdges.isNotEmpty()) {
            appendLine()
            appendLine("    %% ⚠ Architecture violations")
            badEdges.forEach { (f, t) -> appendLine("    ${f.toNodeId()} --> ${t.toNodeId()}") }
            appendLine()
            val idx = (validEdges.size until allEdges.size).joinToString(",")
            appendLine("    linkStyle $idx stroke:#cc0000,stroke-width:2px")
        }

        appendLine()
        appendLine("    style app fill:#e8f4e8,stroke:#2d7a2d,color:#000")
    }
}

fun buildExternalGraph(
    internalDeps: Map<String, List<String>>,
    externalDeps: Map<String, List<String>>,
): String {
    // Collapse :app:components:api + :app:components:impl → a single "components" node.
    // :app stays as its own node.
    fun String.mergedId(): String {
        if (this == ":app") return "app"
        val group = moduleGroup(this)
        if (group != null) return group
        // ":app:something" — container module with 2 segments; fold into its feature group
        val parts = removePrefix(":").split(":")
        if (parts.size == 2 && parts[0] == "app") return parts[1]
        return toNodeId()
    }

    // Union external deps for all modules that belong to the same merged node
    val mergedExtDeps = mutableMapOf<String, MutableSet<String>>()
    externalDeps.forEach { (module, coords) ->
        mergedExtDeps.getOrPut(module.mergedId()) { mutableSetOf() }.addAll(coords)
    }

    // Deps already covered by internal (non-app) modules — app only shows what's unique to it
    val coveredByModules = mergedExtDeps.filterKeys { it != "app" }.values.flatten().toSet()
    mergedExtDeps["app"] = mergedExtDeps["app"]
        ?.filterNot { it in coveredByModules }
        ?.toMutableSet()
        ?: mutableSetOf()

    // Deduplicated inter-group edges (self-loops from api→impl of same group are dropped)
    val mergedEdgesRaw = mutableListOf<Pair<String, String>>()
    internalDeps.forEach { (from, tos) ->
        val fromGroup = from.mergedId()
        tos.forEach { to ->
            val toGroup = to.mergedId()
            if (fromGroup != toGroup) mergedEdgesRaw += fromGroup to toGroup
        }
    }
    val mergedEdges = mergedEdgesRaw.distinct().sortedWith(compareBy({ it.first }, { it.second }))

    val allGroups    = internalDeps.keys.map { it.mergedId() }.distinct().sorted()
    val allExtCoords = mergedExtDeps.values.flatten().distinct()

    return buildString {
        appendLine("flowchart TD")
        appendLine()

        appendLine("    app([\":app\"])")
        appendLine()

        allGroups.filter { it != "app" }.sorted().forEach { group ->
            appendLine("    $group[\"$group\"]")
        }
        appendLine()

        allExtCoords.distinct().sorted().forEach { c ->
            appendLine("    ${extNodeId(c)}(\"${extLabel(c)}\")")
        }
        appendLine()

        mergedEdges.forEach { (f, t) -> appendLine("    $f --> $t") }

        appendLine()
        mergedExtDeps.entries.sortedBy { it.key }.forEach { (module, coords) ->
            coords.sorted().forEach { coord -> appendLine("    $module -.-> ${extNodeId(coord)}") }
        }

        appendLine()
        appendLine("    style app fill:#e8f4e8,stroke:#2d7a2d,color:#000")
        appendLine("    classDef extDep fill:#e8f0ff,stroke:#4466cc,color:#000")
        allExtCoords.distinct().forEach { c -> appendLine("    class ${extNodeId(c)} extDep") }
    }
}

// =============================================================================
// Task registrations
// =============================================================================

fun String.wrapInMermaidFence(): String = "```mermaid\n${this}\n```\n"

tasks.register("generateInternalModuleGraph") {
    group = "documentation"
    description = "Generates INTERNAL_MODULE_GRAPH.md and .mmd — inter-module dependency graph. Red edges = arch violations."

    doLast {
        val diagram = buildInternalGraph(collectInternalDeps())

        // .mmd — opened directly by the Mermaid plugin in Android Studio / IntelliJ
        project.file("INTERNAL_MODULE_GRAPH.mmd").writeText(diagram)

        // .md — rendered by GitHub in PRs and commit views
        project.file("INTERNAL_MODULE_GRAPH.md").writeText(buildString {
            appendLine("# Internal Module Graph")
            appendLine()
            appendLine("> **Generated — do not edit by hand.**")
            appendLine("> Refresh with `./gradlew generateInternalModuleGraph` and commit the result.")
            appendLine(">")
            appendLine("> `:app` is shown as a node but its outgoing edges are hidden —")
            appendLine("> it correctly depends on every module as the composition root.")
            appendLine(">")
            appendLine("> **Red edges** = architecture violation: a module outside `:app`")
            appendLine("> depends on an `:impl` module, which must never be shared.")
            appendLine()
            append(diagram.wrapInMermaidFence())
        })

        logger.lifecycle("✓ INTERNAL_MODULE_GRAPH.mmd + .md written")
    }
}

tasks.register("generateExternalModuleGraph") {
    group = "documentation"
    description = "Generates EXTERNAL_MODULE_GRAPH.md and .mmd — inter-module graph plus direct external library dependencies."

    doLast {
        val diagram = buildExternalGraph(collectInternalDeps(), collectExternalDeps())

        // .mmd — opened directly by the Mermaid plugin in Android Studio / IntelliJ
        project.file("EXTERNAL_MODULE_GRAPH.mmd").writeText(diagram)

        // .md — rendered by GitHub in PRs and commit views
        project.file("EXTERNAL_MODULE_GRAPH.md").writeText(buildString {
            appendLine("# External Module Graph")
            appendLine()
            appendLine("> **Generated — do not edit by hand.**")
            appendLine("> Refresh with `./gradlew generateExternalModuleGraph` and commit the result.")
            appendLine(">")
            appendLine("> **Solid arrows** = internal module dependencies.")
            appendLine("> **Dashed arrows** = direct external library dependencies")
            appendLine("> (BOMs and test-scoped deps excluded).")
            appendLine("> **Blue nodes** = external libraries, grouped by category.")
            appendLine("> **Red solid edges** = architecture violations.")
            appendLine()
            append(diagram.wrapInMermaidFence())
        })

        logger.lifecycle("✓ EXTERNAL_MODULE_GRAPH.mmd + .md written")
    }
}
