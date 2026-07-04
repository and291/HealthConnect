package com.example.healthconnect.integration.permission_overview.ui

import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import com.example.healthconnect.permission_overview.api.ui.PermissionContractProvider

internal class PermissionContractProviderImpl : PermissionContractProvider {

    override fun getLibraryContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    override fun getLibrarySettingsAction(): String {
        return HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
    }
}