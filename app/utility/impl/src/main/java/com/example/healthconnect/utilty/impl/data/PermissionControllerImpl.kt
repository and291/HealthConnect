package com.example.healthconnect.utilty.impl.data

import androidx.health.connect.client.HealthConnectClient
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionController

class PermissionControllerImpl(
    private val healthConnectClient: HealthConnectClient,
) : PermissionController {
    override suspend fun getGrantedPermissions(): Set<String> =
        healthConnectClient.permissionController.getGrantedPermissions()
}
