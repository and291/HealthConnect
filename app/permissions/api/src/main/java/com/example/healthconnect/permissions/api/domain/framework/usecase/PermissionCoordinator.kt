package com.example.healthconnect.permissions.api.domain.framework.usecase

import com.example.healthconnect.permissions.api.domain.framework.PermissionRequest
import com.example.healthconnect.permissions.api.domain.framework.PermissionResult
import com.example.healthconnect.permissions.api.domain.entity.PermissionStatus
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Single entry point for all permission operations.
 *
 * ## VM→Activity bridge design
 *
 * Any ViewModel calls [request], which stores the permission strings into [pendingRequest].
 * MainActivity observes [pendingRequest] and calls `permissionResult.launch(strings)` when
 * the value is non-null. After the system dialog dismisses, MainActivity calls [onActivityResult],
 * which resolves the request by emitting on [results] and clearing [pendingRequest].
 *
 * This keeps all `startActivity` / `registerForActivityResult` code exclusively in MainActivity,
 * matching the pattern used for `requestPermission` and `LibraryNavigation` elsewhere.
 *
 * ## Usage from a ViewModel
 * ```kotlin
 * viewModelScope.launch {
 *     if (coordinator.pendingRequest.value != null) return@launch // guard in-flight
 *     coordinator.request(PermissionRequest.Single(permission))
 *     val result = coordinator.results.first()
 *     // react to result
 * }
 * ```
 */
interface PermissionCoordinator {

    /**
     * The set of permission strings that MainActivity must launch via the
     * `PermissionController` activity-result contract. Non-null when a request is
     * in flight; null when idle.
     *
     * MainActivity should observe this with `filterNotNull().collect { strings -> launch(strings) }`.
     */
    val pendingRequest: StateFlow<Set<String>?>

    /**
     * Emits one [PermissionResult] each time an in-flight request is resolved.
     * Backed by `SharedFlow(replay=0)` — only active subscribers receive results.
     * ViewModels should collect this in `viewModelScope` before calling [request].
     */
    val results: SharedFlow<PermissionResult>

    /**
     * Submits a permission request. If a request is already in flight this call is a no-op.
     * ViewModels should check `pendingRequest.value == null` before calling.
     *
     * Thread-safe; safe to call from any coroutine context.
     */
    suspend fun request(request: PermissionRequest)

    /**
     * Called exclusively by MainActivity after the activity-result contract callback fires.
     * Resolves the in-flight request, emits on [results], and clears [pendingRequest].
     *
     * @param grantedPermissionStrings The set of strings the system reports as currently granted.
     *   The coordinator intersects this with the originally requested set.
     */
    fun onActivityResult(grantedPermissionStrings: Set<String>)

    /**
     * Returns the current grant status of every declared permission without triggering a dialog.
     * Used by [PermissionsScreen][com.example.healthconnect.permissions.impl.ui.screen.PermissionsScreen]
     * to render the overview.
     *
     * Ordered: Read permissions alphabetically, then Write permissions alphabetically.
     */
    suspend fun getPermissionStatuses(): List<PermissionStatus>
}
