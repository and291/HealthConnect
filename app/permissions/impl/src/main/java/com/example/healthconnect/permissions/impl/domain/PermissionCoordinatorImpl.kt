package com.example.healthconnect.permissions.impl.domain

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.permissions.api.domain.framework.HealthPermission
import com.example.healthconnect.permissions.api.domain.framework.PermissionRequest
import com.example.healthconnect.permissions.api.domain.framework.PermissionResult
import com.example.healthconnect.permissions.api.domain.entity.PermissionStatus
import com.example.healthconnect.permissions.api.domain.framework.usecase.LibraryPermissionResolver
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionController
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionCoordinator
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Implementation of [PermissionCoordinator].
 *
 * ## VM→Activity bridge
 * ```
 *  AnyViewModel              PermissionCoordinatorImpl            MainActivity
 *  ────────────              ─────────────────────────            ────────────
 *  request(req) ────────────► _pendingRequest.emit(strings) ─────► observes pendingRequest
 *                                                                   launches activity-result
 *                                                                   contract
 *                                                                   ...dialog...
 *               onActivityResult(granted) ◄──────────────────────── result callback fires
 *               _results.tryEmit(result)
 *  results.first() ◄─────────────────────────────────────────────────
 * ```
 */
class PermissionCoordinatorImpl(
    private val permissionController: PermissionController,
    private val permissionResolver: LibraryPermissionResolver,
    private val allModelTypes: List<KClass<out Model>>,
) : PermissionCoordinator {

    private val mutex = Mutex()
    private var inFlightPermissions: Set<HealthPermission>? = null

    private val _pendingRequest = MutableStateFlow<Set<String>?>(null)
    override val pendingRequest: StateFlow<Set<String>?> = _pendingRequest.asStateFlow()

    // extraBufferCapacity=1: onActivityResult is called from the Activity thread (non-suspend),
    // so tryEmit must not block. One slot is sufficient since only one request is in flight.
    private val _results = MutableSharedFlow<PermissionResult>(
        replay = 0,
        extraBufferCapacity = 1,
    )
    override val results: SharedFlow<PermissionResult> = _results.asSharedFlow()

    override suspend fun request(request: PermissionRequest) {
        mutex.withLock {
            if (inFlightPermissions != null) return  // drop; caller should guard this
            val permissions = request.toSet()
            inFlightPermissions = permissions
            _pendingRequest.emit(permissions.map { it.permissionString }.toSet())
        }
    }

    override fun onActivityResult(grantedPermissionStrings: Set<String>) {
        val requested = inFlightPermissions ?: return

        val granted = requested.filter { it.permissionString in grantedPermissionStrings }.toSet()
        val denied = requested - granted

        val result = when {
            denied.isEmpty()  -> PermissionResult.AllGranted(granted)
            granted.isEmpty() -> PermissionResult.AllDenied(denied)
            else              -> PermissionResult.SomeGranted(granted, denied)
        }

        _results.tryEmit(result)
        // No mutex needed: this runs on the main thread and can't race with itself.
        // Visibility to request() is guaranteed because tryEmit() below establishes
        // a happens-before edge that any coroutine re-entering mutex.withLock() will observe.
        inFlightPermissions = null
        _pendingRequest.tryEmit(null)
    }

    override suspend fun getPermissionStatuses(): List<PermissionStatus> {
        val granted = permissionController.getGrantedPermissions()
        return allModelTypes
            .flatMap { listOf(permissionResolver.readPermission(it), permissionResolver.writePermission(it)) }
            .sortedBy { it.permissionString }
            .map { permission ->
                PermissionStatus(
                    permission = permission,
                    isGranted = permission.permissionString in granted,
                )
            }
    }
}
