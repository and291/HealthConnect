package com.example.healthconnect.permissions.impl.domain

import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission as LibraryHealthPermission
import androidx.health.connect.client.records.Record
import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionRequest
import com.example.healthconnect.permissions.api.domain.PermissionResult
import com.example.healthconnect.permissions.api.domain.PermissionStatus
import com.example.healthconnect.permissions.api.domain.PermissionType
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
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
 *
 * @param permissionController Used only by [getPermissionStatuses] to query current grants.
 *   Obtain via `HealthConnectClient.getOrCreate(context).permissionController`.
 */
class PermissionCoordinatorImpl(
    private val permissionController: PermissionController,
    private val allRecordTypes: List<KClass<out Record>>,
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
        inFlightPermissions = null
        _pendingRequest.tryEmit(null)
    }

    override suspend fun getPermissionStatuses(): List<PermissionStatus> {
        val granted = permissionController.getGrantedPermissions()
        return allRecordTypes
            .flatMap { recordClass ->
                listOf(
                    HealthPermission(LibraryHealthPermission.getReadPermission(recordClass), PermissionType.Read),
                    HealthPermission(LibraryHealthPermission.getWritePermission(recordClass), PermissionType.Write),
                )
            }
            .sortedBy { it.permissionString }
            .map { permission ->
                PermissionStatus(
                    permission = permission,
                    isGranted = permission.permissionString in granted,
                )
            }
    }
}
