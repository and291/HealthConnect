package com.example.healthconnect.permissions.impl.di

import androidx.health.connect.client.permission.HealthPermission as LibraryHealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionRequest
import com.example.healthconnect.permissions.api.domain.PermissionResult
import com.example.healthconnect.permissions.api.domain.PermissionStatus
import com.example.healthconnect.permissions.api.domain.PermissionType
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * No-op [PermissionCoordinator] used when [Di.isPreview] is true.
 * Returns a representative subset of permissions as denied so the Permissions preview
 * renders a realistic state.
 */
internal class FakePermissionCoordinator : PermissionCoordinator {
    override val pendingRequest: StateFlow<Set<String>?> = MutableStateFlow(null)
    override val results: SharedFlow<PermissionResult> = MutableSharedFlow()
    override suspend fun request(request: PermissionRequest) = Unit
    override fun onActivityResult(grantedPermissionStrings: Set<String>) = Unit
    override suspend fun getPermissionStatuses(): List<PermissionStatus> {
        val previewTypes = listOf(
            StepsRecord::class,
            HeartRateRecord::class,
            WeightRecord::class,
            SleepSessionRecord::class,
            BloodPressureRecord::class,
        )
        return previewTypes.flatMap { recordClass ->
            listOf(
                PermissionStatus(HealthPermission(LibraryHealthPermission.getReadPermission(recordClass), PermissionType.Read), isGranted = false),
                PermissionStatus(HealthPermission(LibraryHealthPermission.getWritePermission(recordClass), PermissionType.Write), isGranted = false),
            )
        }
    }
}
