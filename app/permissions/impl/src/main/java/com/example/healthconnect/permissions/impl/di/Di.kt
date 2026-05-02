package com.example.healthconnect.permissions.impl.di

import android.content.Context
import com.example.healthconnect.models.api.domain.record.BloodPressure
import com.example.healthconnect.models.api.domain.record.HeartRate
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.models.api.domain.record.SleepSession
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.models.api.domain.record.Weight
import com.example.healthconnect.permissions.api.domain.framework.HealthPermission
import com.example.healthconnect.permissions.api.domain.framework.PermissionType
import com.example.healthconnect.permissions.api.domain.framework.usecase.LibraryPermissionResolver
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionController
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionCoordinator
import com.example.healthconnect.permissions.impl.domain.PermissionCoordinatorImpl
import com.example.healthconnect.permissions.impl.ui.PermissionsViewModelFactory
import com.example.healthconnect.utilty.api.ui.mapper.RecordTypeNameMapper
import kotlin.reflect.KClass

object Di {
    var isPreview = true
    lateinit var applicationContext: Context

    lateinit var permissionController: PermissionController
    lateinit var permissionResolver: LibraryPermissionResolver
    lateinit var recordTypeNameMapper: RecordTypeNameMapper
    lateinit var allModelTypes: List<KClass<out Model>>

    /**
     * The shared coordinator singleton.
     * Exposed as [PermissionCoordinator] so consumers depend only on the api module.
     *
     * In production: backed by real implementations from utility:impl.
     * In preview: backed by [FakePermissionCoordinator].
     */
    val coordinator: PermissionCoordinator by lazy {
        if (isPreview) {
            FakePermissionCoordinator()
        } else {
            PermissionCoordinatorImpl(
                permissionController = permissionController,
                permissionResolver = permissionResolver,
                allModelTypes = allModelTypes,
            )
        }
    }

    private val previewModelTypes = listOf(Steps::class, HeartRate::class, Weight::class, SleepSession::class, BloodPressure::class)

    private val fakePermissionResolver = object : LibraryPermissionResolver {
        override fun readPermission(type: KClass<out Model>) =
            HealthPermission("fake:read:${type.simpleName!!.lowercase()}", PermissionType.Read)
        override fun writePermission(type: KClass<out Model>) =
            HealthPermission("fake:write:${type.simpleName!!.lowercase()}", PermissionType.Write)
    }

    private val fakeRecordTypeNameMapper = object : RecordTypeNameMapper {
        override fun nameRes(type: KClass<out Model>): Int = android.R.string.ok
    }

    val permissionsViewModelFactory: PermissionsViewModelFactory by lazy {
        if (isPreview) {
            PermissionsViewModelFactory(coordinator, previewModelTypes, fakeRecordTypeNameMapper, fakePermissionResolver)
        } else {
            PermissionsViewModelFactory(coordinator, allModelTypes, recordTypeNameMapper, permissionResolver)
        }
    }
}
