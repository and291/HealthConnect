package com.example.healthconnect.integration.permission_overview.data

import androidx.health.connect.client.PermissionController
import com.example.healthconnect.permission_overview.api.domain.PermissionResolver
import com.example.healthconnect.permission_overview.api.domain.entity.IsGranted
import com.example.healthconnect.permission_overview.api.domain.entity.Permission

class PermissionResolverImpl(
    private val controller: PermissionController
) : PermissionResolver {

    override suspend fun isGranted(permission: Permission): Boolean {
        return controller.getGrantedPermissions().contains(permission.value)
    }

    override suspend fun isGranted(permissions: Set<Permission>): Set<Pair<Permission, IsGranted>> {
        val granted = controller.getGrantedPermissions()
        return permissions.map { it to IsGranted(granted.contains(it.value)) }.toSet()
    }
}
