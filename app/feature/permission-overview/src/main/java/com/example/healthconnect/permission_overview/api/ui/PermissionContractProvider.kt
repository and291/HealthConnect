package com.example.healthconnect.permission_overview.api.ui

import androidx.activity.result.contract.ActivityResultContract

interface PermissionContractProvider {
    fun getLibraryContract(): ActivityResultContract<Set<String>, Set<String>>
    fun getLibrarySettingsAction(): String
}