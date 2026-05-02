package com.example.healthconnect.permissions.api.usecase

interface PermissionController {
    suspend fun getGrantedPermissions(): Set<String>
}
