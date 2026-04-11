# Module Graph Script — Agent Specification

> This document is the authoritative specification for `gradle/module-graph.gradle.kts`.
> Use it to regenerate, extend, or modify the script correctly.

---

## Overview

`gradle/module-graph.gradle.kts` is a Gradle **script plugin** applied from the root
`build.gradle.kts` via:

```kotlin
apply(from = "gradle/module-graph.gradle.kts")
```

It registers two documentation tasks on the **root project**:

| Task | Output files |
|---|---|
| `./gradlew generateInternalModuleGraph` | `INTERNAL_MODULE_GRAPH.md`, `INTERNAL_MODULE_GRAPH.mmd` |
| `./gradlew generateExternalModuleGraph` | `EXTERNAL_MODULE_GRAPH.md`, `EXTERNAL_MODULE_GRAPH.mmd` |

Both files are written to the **repo root** and must be committed. The `.md` file is
rendered by GitHub; the `.mmd` file is opened directly by the Mermaid plugin in
Android Studio / IntelliJ (plugin requires the raw `.mmd` extension, not a fenced
block inside Markdown).

Tasks are placed in Gradle group `"documentation"`. They re-read the live Gradle
model on every run — no caching, always accurate.

---

## Gradle API constraints

- **Requires `import org.gradle.api.artifacts.ProjectDependency`** at the top of the
  script — this type is used to distinguish project-to-project dependencies from
  external ones.
- **`dep.path` not `dep.dependencyProject.path`** — `getDependencyProject()` was
  removed in Gradle 8.4. The replacement is `ProjectDependency.path`.
- **`sub.configurations.findByName(cfgName)`** — use `findByName`, not `getByName`,
  because not every configuration is present in every module.
- **Avoid Kotlin functional chains (`flatMap`, `map`) with local extension functions
  in nested lambdas** — the Kotlin compiler used by Gradle script compilation cannot
  always infer types correctly in that context. Use explicit `forEach` loops instead.

---

## Data collection

### `graphedConfigs`

```kotlin
val graphedConfigs = listOf("implementation", "api", "compileOnly")
```

Only these three configurations are scanned. Excluded: `testImplementation`,
`androidTestImplementation`, `debugImplementation`, `runtimeOnly`.

### `collectInternalDeps(): Map<String, List<String>>`

Returns `{ module path → [depended-upon module paths] }` for **all subprojects**.

- Iterates `subprojects` (all Gradle subprojects of the root project).
- For each subproject, iterates `graphedConfigs`, finds each configuration with
  `findByName`, iterates its `dependencies`.
- Only keeps dependencies where `dep is ProjectDependency` (module-to-module).
- Uses `dep.path` to get the target module's Gradle path (e.g. `:app:components:api`).
- Returns `distinct()` paths per module.
- Every subproject is always a key in the map, even if its value is an empty list.

### `collectExternalDeps(): Map<String, List<String>>`

Returns `{ module path → ["group:name", ...] }` for **all subprojects**.

- Same iteration as `collectInternalDeps`.
- Only keeps dependencies where `dep !is ProjectDependency` AND `dep.group != null`
  AND `dep.name` does not contain `"bom"`.
- Coordinate format: `"${dep.group}:${dep.name}"` — no version.
- BOMs are excluded because they influence resolution, not the dependency graph.
- Returns `distinct()` coordinates per module.

---

## Module path conventions

The project uses a 3-level path hierarchy:

```
:app                          ← composition root (special case)
:app:<feature>                ← container module (Gradle subproject, no sources)
:app:<feature>:api            ← public API module
:app:<feature>:impl           ← implementation module
```

Examples: `:app:components:api`, `:app:editor:impl`, `:app:models:api`,
`:app:navigation:api`, `:app:utility:api`, `:app:utility:impl`.

---

## Shared helper functions

### `String.toNodeId(): String`
Produces a stable, Mermaid-safe node identifier from a module path.
```kotlin
removePrefix(":").replace(Regex("[^a-zA-Z0-9]"), "_")
```
Example: `:app:components:api` → `app_components_api`

### `String.toLabel(): String`
Short human-readable label shown inside an internal-graph node box.
```kotlin
removePrefix(":app:").ifEmpty { "app" }
```
Example: `:app:components:api` → `components:api`, `:app` → `app`

### `moduleGroup(path: String): String?`
Extracts the feature-group name from a 3-segment module path.
```kotlin
path.removePrefix(":").split(":").takeIf { it.size >= 3 }?.get(1)
```
- `:app:components:api` → `"components"`
- `:app:editor:impl` → `"editor"`
- `:app:components` (2 segments) → `null`
- `:app` → `null`

### `isViolation(from: String, to: String): Boolean`
Architecture rule: any module **other than `:app`** must not depend on an `:impl`
module.
```kotlin
from != ":app" && to.endsWith(":impl")
```
Rationale: `:impl` modules are private. Only the paired `:api` module may be shared
across feature boundaries. `:app` is the composition root and is exempt — it
intentionally wires every module together.

### External dependency helpers

```kotlin
fun extNodeId(coord: String): String =
    "ext_" + coord.replace(Regex("[^a-zA-Z0-9]"), "_")
// "androidx.compose.ui:ui" → "ext_androidx_compose_ui_ui"

fun extLabel(coord: String): String = coord.substringAfter(":")
// "androidx.compose.ui:ui" → "ui"

fun extCategory(coord: String): String   // unused in current output, kept for future use
```

`extCategory` maps a Maven group to a display category string (`"Compose"`,
`"Health Connect"`, `"Lifecycle"`, `"Navigation"`, `"Core"`, `"Kotlin"`, `"Other"`).
It is defined but not referenced in either graph builder — it exists for a future
subgraph-grouping feature.

---

## Internal graph — `buildInternalGraph`

**Input:** `deps: Map<String, List<String>>` from `collectInternalDeps()`

**Output:** raw Mermaid `flowchart TD` string (no fences).

### What is shown

- `:app` as a stadium-shaped node (`([":app"])`) with green fill.
- All other modules grouped into `subgraph` blocks by feature group (`moduleGroup`).
- Container modules (2-segment paths, `moduleGroup` returns null) are included in
  the node list but not placed in any subgraph — they appear at the top level.
- Inter-module edges **excluding** any edge where `from == ":app"` or `to == ":app"`.
  `:app`'s outgoing edges are correct by definition (it is the composition root) and
  are hidden for diagram clarity.

### Edge colouring

Edges are split into `validEdges` and `badEdges` (`isViolation` check). Both lists
are emitted as `-->` edges. Valid edges are emitted first because `linkStyle` is
**index-based** in Mermaid — the index of the first bad edge equals `validEdges.size`.

```kotlin
val idx = (validEdges.size until allEdges.size).joinToString(",")
appendLine("    linkStyle $idx stroke:#cc0000,stroke-width:2px")
```

If there are no violations, no `linkStyle` line is emitted.

### Node declaration

Nodes are declared inside `subgraph` blocks:
```
subgraph components
    app_components_api["components:api"]
    app_components_impl["components:impl"]
end
```

The subgraph label is the raw group name (e.g. `components`). Node IDs use
`toNodeId()`. Node labels use `toLabel()`.

---

## External graph — `buildExternalGraph`

**Input:**
- `internalDeps: Map<String, List<String>>` from `collectInternalDeps()`
- `externalDeps: Map<String, List<String>>` from `collectExternalDeps()`

**Output:** raw Mermaid `flowchart TD` string (no fences).

### Merging api + impl into a single node

The external graph collapses every `api`/`impl` pair (and their container module)
into a single feature-group node. This is done via the local `mergedId()` function,
defined inside `buildExternalGraph`:

```kotlin
fun String.mergedId(): String {
    if (this == ":app") return "app"
    val group = moduleGroup(this)          // handles 3-segment paths
    if (group != null) return group
    // 2-segment container modules like ":app:components" — fold into feature group
    val parts = removePrefix(":").split(":")
    if (parts.size == 2 && parts[0] == "app") return parts[1]
    return toNodeId()                      // fallback for unexpected shapes
}
```

All three of `:app:components`, `:app:components:api`, `:app:components:impl` map
to `"components"`. The node ID in the diagram is the plain group name.

### External dependency merging

The external deps of all modules that belong to the same merged group are unioned
into a single set:

```kotlin
val mergedExtDeps = mutableMapOf<String, MutableSet<String>>()
externalDeps.forEach { (module, coords) ->
    mergedExtDeps.getOrPut(module.mergedId()) { mutableSetOf() }.addAll(coords)
}
```

### App deduplication

`:app` only shows external dependencies that are **not already covered** by any
other feature-group module. This prevents the app node from being cluttered with
re-declarations of what utility/editor/etc. already depend on.

```kotlin
val coveredByModules = mergedExtDeps.filterKeys { it != "app" }.values.flatten().toSet()
mergedExtDeps["app"] = mergedExtDeps["app"]
    ?.filterNot { it in coveredByModules }
    ?.toMutableSet()
    ?: mutableSetOf()
```

### Internal edges (inter-group)

Computed with explicit nested `forEach` loops — **never use `flatMap + map` with
`mergedId()`** because the Gradle Kotlin compiler fails to resolve the local
extension inside a nested lambda:

```kotlin
val mergedEdgesRaw = mutableListOf<Pair<String, String>>()
internalDeps.forEach { (from, tos) ->
    val fromGroup = from.mergedId()
    tos.forEach { to ->
        val toGroup = to.mergedId()
        if (fromGroup != toGroup) mergedEdgesRaw += fromGroup to toGroup
    }
}
val mergedEdges = mergedEdgesRaw.distinct().sortedWith(compareBy({ it.first }, { it.second }))
```

Self-loops (api → impl of the same group) are dropped by the `fromGroup != toGroup`
check.

### Node declarations

```
app([":app"])                         ← stadium shape, green fill
components["components"]              ← rectangle, no fill (plain internal node)
ext_androidx_compose_ui_ui("ui")      ← rounded rectangle, blue fill (extDep class)
```

External nodes use rounded-rectangle shape (`()`  in Mermaid), styled with:
```
classDef extDep fill:#e8f0ff,stroke:#4466cc,color:#000
class <nodeId> extDep
```

### Edge types

```
components --> editor        ← solid arrow, inter-module dependency
app -.-> ext_foo_bar         ← dashed arrow, external library dependency
```

No red edges in the external graph — architecture violations are only shown in the
internal graph.

---

## Output format

### `.mmd` file

Raw Mermaid diagram, no wrapping. Written with `project.file("FOO.mmd").writeText(diagram)`.
Opened directly by the JetBrains Mermaid plugin in Android Studio.

Supported diagram types verified with the plugin: `flowchart`, `classDiagram`,
`erDiagram`, `sequenceDiagram`, `stateDiagram`, `stateDiagram-v2`, `gantt`,
`gitGraph`, `mindmap`, `quadrantChart`, `requirementDiagram`, `sankey-beta`,
`xychart-beta`, `C4Container`, `C4Dynamic`.
**Current choice: `flowchart TD`.**

### `.md` file

Markdown header + blockquote legend + Mermaid fenced block. Written with:

```kotlin
fun String.wrapInMermaidFence(): String = "```mermaid\n${this}\n```\n"
```

Rendered by GitHub in PR diffs and commit views.

---

## Task registration boilerplate

Each task follows this pattern:

```kotlin
tasks.register("generateXxxModuleGraph") {
    group = "documentation"
    description = "..."
    doLast {
        val diagram = buildXxxGraph(...)
        project.file("XXX_MODULE_GRAPH.mmd").writeText(diagram)
        project.file("XXX_MODULE_GRAPH.md").writeText(buildString {
            appendLine("# Xxx Module Graph")
            appendLine()
            appendLine("> **Generated — do not edit by hand.**")
            appendLine("> Refresh with `./gradlew generateXxxModuleGraph` and commit the result.")
            // ... additional legend lines ...
            appendLine()
            append(diagram.wrapInMermaidFence())
        })
        logger.lifecycle("✓ XXX_MODULE_GRAPH.mmd + .md written")
    }
}
```

All file I/O happens inside `doLast`. Data collection (`collectInternalDeps()`,
`collectExternalDeps()`) is also called inside `doLast` — not at configuration time.

---

## Extension guide

### Add a new task (e.g. a per-feature subgraph)
1. Write a `buildFooGraph(deps)` function following the same pattern.
2. Register `tasks.register("generateFooModuleGraph") { ... }`.
3. No changes needed to `build.gradle.kts` — the script plugin exposes all tasks
   automatically once applied.

### Add a new architecture rule
Add a condition to `isViolation(from, to)` or write a separate check function.
Emit violation edges after valid ones and extend the `linkStyle` index range.

### Change the diagram type
Replace `"flowchart TD"` with another supported type. Node and edge syntax must be
updated to match the new type's grammar. `linkStyle` is specific to `flowchart` —
remove it if switching away.

### Add subgraph grouping for external deps
`extCategory(coord)` already classifies external coords into categories. Wire it
into `buildExternalGraph` by grouping `allExtCoords` by category and wrapping each
group in a `subgraph` block, following the same pattern as `buildInternalGraph`.

### Change which configs are scanned
Edit `graphedConfigs`. Adding `"runtimeOnly"` would include runtime-only deps.
Removing `"compileOnly"` would hide annotation processors and similar.
