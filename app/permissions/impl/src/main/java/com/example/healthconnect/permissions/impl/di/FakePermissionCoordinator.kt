package com.example.healthconnect.permissions.impl.di

import com.example.healthconnect.models.api.domain.record.BloodPressure
import com.example.healthconnect.models.api.domain.record.HeartRate
import com.example.healthconnect.models.api.domain.record.SleepSession
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.models.api.domain.record.Weight
import com.example.healthconnect.permissions.api.domain.framework.HealthPermission
import com.example.healthconnect.permissions.api.domain.framework.PermissionRequest
import com.example.healthconnect.permissions.api.domain.framework.PermissionResult
import com.example.healthconnect.permissions.api.domain.entity.PermissionStatus
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionCoordinator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * No-op [PermissionCoordinator] used when [Di.isPreview] is true.
 * Returns a representative subset of permissions as denied so the Permissions preview
 * renders a realistic state.
 *
 * Permission strings use the same scheme as [Di.fakePermissionResolver] so the ViewModel's
 * name lookup succeeds in preview.
 */
internal class FakePermissionCoordinator : PermissionCoordinator {
    override val pendingRequest: StateFlow<Set<String>?> = MutableStateFlow(null)
    override val results: SharedFlow<PermissionResult> = MutableSharedFlow()
    override suspend fun request(request: PermissionRequest) = Unit
    override fun onActivityResult(grantedPermissionStrings: Set<String>) = Unit
    override suspend fun getPermissionStatuses(): List<PermissionStatus> {
        val previewTypes = listOf(Steps::class, HeartRate::class, Weight::class, SleepSession::class, BloodPressure::class)
        return previewTypes.flatMap { type ->
            listOf(
                PermissionStatus(HealthPermission("fake:read:${type.simpleName!!.lowercase()}"), isGranted = false),
                PermissionStatus(HealthPermission("fake:write:${type.simpleName!!.lowercase()}"), isGranted = false),
            )
        }
    }
}
