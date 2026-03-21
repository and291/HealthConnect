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

### Dependency Injection

Manual service locator pattern using `object Di {}` singletons with lazy initialization. Each module has its own `Di.kt`. The main app wires everything together in `MainActivity.onCreate()`:

```kotlin
Di.also {
    it.isPreview = false
    it.applicationContext = this.application
}
```

The `isPreview` flag switches between real and mock repository implementations (used for Compose previews).

### Navigation

Uses **Jetpack Navigation 3** with custom sealed class-based routes (`AppNavigationEntry`, `UtilityNavigationEntry`, `EditorNavigationEntry`). Navigation is stack-based. Entry decorators handle ViewModel persistence and saved state across navigation events.

### Data Flow

```
Compose UI → ViewModel → Use Cases (Read/InsertImpl/UpdateImpl/Delete)
                              ↓
                        LibraryRepository (interface)
                              ↓
                        HealthConnectClient (Android SDK)
```

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

Unit tests live under `app/src/test/`. Key test files cover mappers (`RecordTypeIconMapperTest`, `RecordTypeNameMapperTest`) and domain logic (`LibraryRecordsTest`). Instrumentation tests use Espresso but have minimal coverage currently.
