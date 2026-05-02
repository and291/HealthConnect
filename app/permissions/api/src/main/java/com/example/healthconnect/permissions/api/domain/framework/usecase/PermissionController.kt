package com.example.healthconnect.permissions.api.domain.framework.usecase

interface PermissionController {
    suspend fun getGrantedPermissions(): Set<String>
}
