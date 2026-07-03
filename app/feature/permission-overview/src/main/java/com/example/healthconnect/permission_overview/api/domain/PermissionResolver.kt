package com.example.healthconnect.permission_overview.api.domain

import com.example.healthconnect.permission_overview.api.domain.entity.IsGranted
import com.example.healthconnect.permission_overview.api.domain.entity.Permission

interface PermissionResolver {

    suspend fun isGranted(permission: Permission): Boolean

    suspend fun isGranted(permissions: Set<Permission>): Set<Pair<Permission, IsGranted>>
}