package com.example.healthconnect.utilty.impl.data

import androidx.health.connect.client.permission.HealthPermission as LibraryHealthPermission
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.permissions.api.domain.framework.HealthPermission
import com.example.healthconnect.permissions.api.domain.framework.PermissionType
import com.example.healthconnect.permissions.api.domain.framework.usecase.LibraryPermissionResolver
import com.example.healthconnect.utilty.impl.data.mapper.TypeMapper
import kotlin.reflect.KClass

class LibraryPermissionResolverImpl(
    private val typeMapper: TypeMapper,
) : LibraryPermissionResolver {

    override fun readPermission(type: KClass<out Model>): HealthPermission =
        HealthPermission(
            permissionString = LibraryHealthPermission.getReadPermission(typeMapper.toRecord(type)),
            type = PermissionType.Read,
        )

    override fun writePermission(type: KClass<out Model>): HealthPermission =
        HealthPermission(
            permissionString = LibraryHealthPermission.getWritePermission(typeMapper.toRecord(type)),
            type = PermissionType.Write,
        )
}
