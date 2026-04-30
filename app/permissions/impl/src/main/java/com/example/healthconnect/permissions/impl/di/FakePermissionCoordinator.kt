package com.example.healthconnect.permissions.impl.di

import com.example.healthconnect.permissions.api.domain.PermissionRequest
import com.example.healthconnect.permissions.api.domain.PermissionResult
import com.example.healthconnect.permissions.api.domain.PermissionStatus
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import com.example.healthconnect.permissions.impl.data.AllHealthPermissions
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * No-op [PermissionCoordinator] used when [Di.isPreview] is true.
 * Returns all permissions as denied so the Permissions preview renders a realistic state.
 */
internal class FakePermissionCoordinator : PermissionCoordinator {
    override val pendingRequest: StateFlow<Set<String>?> = MutableStateFlow(null)
    override val results: SharedFlow<PermissionResult> = MutableSharedFlow()
    override suspend fun request(request: PermissionRequest) = Unit
    override fun onActivityResult(grantedPermissionStrings: Set<String>) = Unit
    override suspend fun getPermissionStatuses(): List<PermissionStatus> =
        AllHealthPermissions.all.map { PermissionStatus(it, isGranted = false) }
}
