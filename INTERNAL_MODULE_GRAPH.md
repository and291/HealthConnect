# Internal Module Graph

> **Generated — do not edit by hand.**
> Refresh with `./gradlew generateInternalModuleGraph` and commit the result.
>
> `:app` is shown as a node but its outgoing edges are hidden —
> it correctly depends on every module as the composition root.
>
> **Red edges** = architecture violation: a module outside `:app`
> depends on an `:impl` module, which must never be shared.

```mermaid
flowchart TD

    app([":app"])

    subgraph components
        app_components_api["components:api"]
        app_components_impl["components:impl"]
    end

    subgraph editor
        app_editor_api["editor:api"]
        app_editor_impl["editor:impl"]
    end

    subgraph models
        app_models_api["models:api"]
    end

    subgraph navigation
        app_navigation_api["navigation:api"]
    end

    subgraph utility
        app_utility_api["utility:api"]
        app_utility_impl["utility:impl"]
    end

    app_components_impl --> app_components_api
    app_editor_api --> app_components_api
    app_editor_api --> app_navigation_api
    app_editor_api --> app_models_api
    app_editor_impl --> app_components_api
    app_editor_impl --> app_utility_api
    app_editor_impl --> app_editor_api
    app_editor_impl --> app_navigation_api
    app_editor_impl --> app_models_api
    app_models_api --> app_components_api
    app_utility_api --> app_navigation_api
    app_utility_api --> app_models_api
    app_utility_impl --> app_utility_api
    app_utility_impl --> app_editor_api
    app_utility_impl --> app_navigation_api
    app_utility_impl --> app_components_api
    app_utility_impl --> app_models_api

    style app fill:#e8f4e8,stroke:#2d7a2d,color:#000

```
