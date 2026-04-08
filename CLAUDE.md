# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**HealthConnect Test Utility** is an Android diagnostic app for exploring and verifying data stored in [Android Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect). It supports reading, writing, editing, and deleting health records across 80+ Health Connect record types.

## Build Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew installDebug           # Build and install on connected device/emulator
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumentation tests (requires device)
./gradlew lint                   # Run lint checks
./gradlew clean build            # Clean build
```

Run a single test class:
```bash
./gradlew :app:test --tests "com.example.healthconnect.SomeTestClass"
```

## Architectural Principles

**Always prefer the architecturally correct approach over the minimal one.** A smaller diff is not a goal if it means bypassing established patterns. When in doubt about how to implement something, look at how similar things are already done in the codebase and follow that pattern — even if it requires touching more files.

Concrete implications:
- Do not use `LocalContext.current` inside composables to fire Android side effects (`startActivity`, etc.). Side effects that require an `Activity` or `Context` must be invoked at the Activity layer and passed down as `() -> Unit` callbacks.
- Do not import platform SDK types (e.g. `HealthConnectClient`) inside UI composables. The UI layer must not depend on the data/platform layer directly.
- User interactions in composables must flow through the ViewModel as `Event`s, not call platform APIs directly.
- Navigation and external intent launching are side effects and must be modelled as `Effect`s emitted by the ViewModel, not triggered inline in `onClick` handlers.

The architecture is actively evolving. When a new pattern is introduced it supersedes older code that may not yet follow it.

## Architecture

The project is a **multi-module Android app** using Jetpack Compose, organized around API/implementation module pairs:

```
app/                   → Main app module (orchestration, DI wiring, navigation)
app/components/api     → Field editor component interfaces
app/components/impl    → Field editor component implementations
app/editor/api         → Record editor interfaces
app/editor/impl        → Record editor implementations (insert/edit screens)
app/utility/api        → Records browser interfaces
app/utility/impl       → Records browser + Health Connect data access
app/navigation/api     → Navigation entry types (sealed classes)
```

**Module dependency rule:** `impl` modules depend on their own `api` module and may depend on other `api` modules. The main `app` module depends on all modules. Circular dependencies are not allowed.

### MVI Pattern (Event / State / Effect)

Every screen follows a strict MVI pattern. ViewModels expose three things:

- **`state`** — a `mutableStateOf` value representing what the UI should currently render. Updated synchronously inside the ViewModel.
- **`effect`** — a `MutableStateFlow<Effect?>` for one-time side effects (navigation, launching external intents, showing dialogs). Always nullable; cleared after consumption.
- **`onEvent(event)`** — the single entry point for all UI interactions.

```kotlin
// Canonical shape of a ViewModel
class FooViewModel(...) : ViewModel() {

    private var _state by mutableStateOf<State>(State.Loading)
    val state: State get() = _state

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) _effect.value = null
    }

    fun onEvent(event: Event) {
        when (event) { ... }
    }

    sealed class State { ... }
    sealed class Effect { ... }
    sealed class Event { ... }
}
```

#### Effect consumption in composables

Effects are collected as state and handled in a `LaunchedEffect`. After handling, the composable calls `effectConsumed()` to clear the effect:

```kotlin
val effect by viewModel.effect.collectAsState(null)
LaunchedEffect(effect) {
    effect?.let { e ->
        when (e) {
            is Effect.NavigateToRecords -> onTypeClick(e.recordType, e.nameRes)
            is Effect.ShowLibraryDataManager -> onShowLibraryDataManager()
        }
        viewModel.effectConsumed(e)
    }
}
```

Effects that require platform side effects (launching an `Activity`, requesting a permission) are modelled as navigation/action callbacks passed into the composable from above — never executed inside the composable itself.

### Navigation

Uses **Jetpack Navigation 3** with a custom sealed class-based route hierarchy. Navigation is back-stack driven — `SnapshotStateList<NavigationEntry>` is the source of truth.

Route hierarchy:
- `AppNavigationEntry` (`Splash`, `Unavailable`, `ProviderUpdateRequired`) — app-level screens, handled in `NavDisplayFactory`
- `UtilityNavigationEntry` (`Dashboard`, `Records`) — records browser, handled in `UtilityNavigationEntryProviderImpl`
- `EditorNavigationEntry` (`EditRecordScreen`, `Insert`) — record editor, handled in `EditorNavigationEntryProviderImpl`

Screens never push to the back stack directly. They emit an `Effect` which is handled by the `NavigationEntryProvider`, which performs the `backStack.add(...)`.

#### Callback propagation chain

Platform-level actions (launching external intents, requesting permissions) originate in `MainActivity` and are passed down as lambdas through a fixed chain:

```
MainActivity
  └─ CreateNavDisplay(activity, libraryNavigation, ...)
       └─ UtilityNavigationEntryProvider.getNavEntry(showInternalDataManager, ...)
            └─ DashboardScreen(onShowInternalDataManager, ...)
                 └─ ViewModel emits Effect → composable calls onShowInternalDataManager()
```

Each layer only receives the `() -> Unit` lambda it needs — it never receives the `Activity` or `LibraryNavigation` directly.

### Platform Side Effects and LibraryNavigation

`LibraryNavigation` (`app/ui/navigation/LibraryNavigation.kt`) is the single place where Health Connect-related `Intent`s are constructed. It holds `applicationContext` and exposes ready-to-use intents:

```kotlin
class LibraryNavigation(private val applicationContext: Context) {
    fun chooseManageDataIntent(): Intent  // HealthConnectClient.getHealthConnectManageDataIntent
    fun chooseUpdateLibraryIntent(): Intent  // Play Store update intent
}
```

All `startActivity` calls happen in `MainActivity` or `NavDisplayFactory` — never in composables, ViewModels, or use cases. `LibraryNavigation` is wired through `Di` (main app module) and passed into `CreateNavDisplay`.

### Dependency Injection

Manual service locator pattern using `object Di {}` singletons with lazy initialization. Each module has its own `Di.kt`. Cross-module dependencies are wired explicitly in `MainActivity.onCreate()`:

```kotlin
// Main app Di is initialised first
Di.also {
    it.isPreview = false
    it.applicationContext = this.application
}
// Utility module Di receives context and editor's ModelFactory
com.example.healthconnect.utilty.impl.di.Di.also {
    it.isPreview = false
    it.applicationContext = this.application
    it.modelFactory = com.example.healthconnect.editor.impl.di.Di.modelFactory
}
// Editor module Di receives components and use cases from utility
com.example.healthconnect.editor.impl.di.Di.also {
    it.fieldProvider = com.example.healthconnect.components.impl.di.Di.fieldProvider
    it.update = com.example.healthconnect.utilty.impl.di.Di.update
    it.insert = com.example.healthconnect.utilty.impl.di.Di.insert
}
```

The `isPreview` flag switches between the real `LibraryRepositoryImpl` and an anonymous mock that returns controlled data (used for Compose previews and avoids real Health Connect calls).

### Data Flow

```
Compose UI
  └─ Event → ViewModel
               └─ Use Cases (Read / InsertImpl / UpdateImpl / Delete)
                    └─ LibraryRepository (interface)
                         └─ LibraryRepositoryImpl → HealthConnectClient (Android SDK)
```

There are two `LibraryRepository` interfaces: a minimal one in the main `app` module (used by `ActivityViewModel`/`SdkPermissionsViewModel`) and a full one in `utility/impl` that includes read/insert/update/delete. `LibraryRepositoryImpl` implements the full interface.

Mappers transform between Health Connect SDK types and UI/domain models: `PayloadMapper`, `ResultMapper`, `RecordTypeIconMapper`, `RecordTypeNameMapper`, `MetadataMapper`.

### State Management

ViewModels use Compose's `mutableStateOf` / `mutableStateListOf`. No third-party state management library.

## Key Technologies

- **Kotlin 2.3.20**, **AGP 9.0.1**, **compileSdk 36**, **minSdk 28** (utility modules)
- **Jetpack Compose BOM 2026.03.00**, **Material3**
- **Navigation 3** (Jetpack, version 1.0.1) — note: this is newer Navigation API, not Navigation 2
- **Health Connect SDK**: `androidx.health:connect-client:1.2.0-alpha02`
- **Kotlin Serialization** plugin enabled
- Versions managed via `gradle/libs.versions.toml`

## Compose Previews

Every `@Composable` function in the project must have at least one fully operational and interactive `@Preview`. Previews are placed after the composable they demonstrate in the same file, declared `private`. Preview functions must be self-contained — pass explicit sample data for stateless composables, and rely on `Di` (which defaults to `isPreview = true` with a mock repository) for screen-level composables that require a ViewModel.

For interactive preview correctness, the mock `LibraryRepository` in `Di` returns `Result.PermissionRequired` for all record types (since the mock grants only `"sdk:permission"`, not the real Health Connect permissions), so all counts render as `null` in previews.

## Testing

The project is developed using a **TDD (Test-Driven Development)** approach. Tests should be written before or alongside the implementation, not as an afterthought.

Unit tests live under `app/src/test/`. Key test files cover mappers (`RecordTypeIconMapperTest`, `RecordTypeNameMapperTest`) and domain logic (`LibraryRecordsTest`). Instrumentation tests use Espresso but have minimal coverage currently.

### What makes a test useful

A test is useful only if it can fail due to a real mistake in the implementation. Before writing a test, ask: *what bug would this catch that the compiler wouldn't?*

**Avoid mirror tests** — tests whose expected values are a copy of the implementation's internal data. A test that duplicates a lookup map entry-for-entry catches nothing; any mistake made consistently in both places passes silently. Example of a useless test:

```kotlin
// Implementation
private val map = mapOf(Steps::class to StepsRecord::class, ...)

// Test — mirrors the map, catches nothing
assertEquals(StepsRecord::class, mapper.map(Steps::class))
```

**Prefer completeness tests** — tests that verify coverage against an independent source of truth. For example, asserting that every entry in `SupportedRecords` has a corresponding mapper entry catches omissions when new record types are added:

```kotlin
@Test
fun `all supported record types are covered`() {
    val allTypes = SupportedRecords.instantaneous + SupportedRecords.interval + SupportedRecords.series
    allTypes.forEach { mapper.someMethod(it) } // throws if missing
}
```

If the only meaningful thing a test validates is a non-obvious name mismatch (e.g. `BloodGlucoseLevel` → `BloodGlucoseRecord`), consider whether a code comment on that entry is more appropriate than a full test class.
