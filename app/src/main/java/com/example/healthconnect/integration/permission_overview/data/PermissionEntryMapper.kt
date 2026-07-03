package com.example.healthconnect.integration.permission_overview.data

import com.example.healthconnect.permission_overview.api.domain.entity.Permission
import com.example.healthconnect.permission_overview.api.domain.entity.PermissionEntry
import com.example.healthconnect.permission_overview.api.domain.entity.ReadWrite
import com.example.healthconnect.utilty.api.data.LibraryPermission

class PermissionEntryMapper {
    fun map(
        permissions: Set<LibraryPermission>,
    ): Set<PermissionEntry> = permissions.map { permission ->
        ReadWrite(
            nameResId = permission.nameResId,
            read = Permission(permission.read),
            write = Permission(permission.write)
        )
    }.toSet()
}
