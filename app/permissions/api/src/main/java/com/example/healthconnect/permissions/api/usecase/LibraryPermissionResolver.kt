package com.example.healthconnect.permissions.api.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.permissions.api.domain.HealthPermission
import kotlin.reflect.KClass

interface LibraryPermissionResolver {
    fun readPermission(type: KClass<out Model>): HealthPermission
    fun writePermission(type: KClass<out Model>): HealthPermission
}
