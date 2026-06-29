package com.example.healthconnect.utilty.impl.data

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import kotlin.reflect.KClass

internal class LibraryPermissionDataSource {

    fun getReadPermission(recordKlass: KClass<out Record>): String =
        HealthPermission.getReadPermission(recordKlass)

    fun getWritePermission(recordKlass: KClass<out Record>): String =
        HealthPermission.getWritePermission(recordKlass)

    @Suppress("unused")
    inline fun <reified T : Record> getReadPermission(): String =
        HealthPermission.getReadPermission<T>()

    @Suppress("unused")
    inline fun <reified T : Record> getWritePermission(): String =
        HealthPermission.getWritePermission<T>()
}