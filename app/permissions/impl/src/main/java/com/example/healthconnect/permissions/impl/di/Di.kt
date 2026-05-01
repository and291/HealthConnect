package com.example.healthconnect.permissions.impl.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import com.example.healthconnect.permissions.api.navigation.PermissionNavigationEntryProvider
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import com.example.healthconnect.permissions.impl.domain.PermissionCoordinatorImpl
import com.example.healthconnect.permissions.impl.navigation.PermissionNavigationEntryProviderImpl
import com.example.healthconnect.permissions.impl.ui.PermissionsViewModelFactory
import kotlin.reflect.KClass

object Di {
    var isPreview = true
    lateinit var applicationContext: Context
    lateinit var allRecordTypes: List<KClass<out Record>>

    /**
     * The shared coordinator singleton.
     * Exposed as [PermissionCoordinator] so consumers depend only on the api module.
     *
     * In production: backed by a real [HealthConnectClient].
     * In preview: backed by [FakePermissionCoordinator].
     */
    val coordinator: PermissionCoordinator by lazy {
        if (isPreview) {
            FakePermissionCoordinator()
        } else {
            PermissionCoordinatorImpl(
                permissionController = HealthConnectClient.getOrCreate(applicationContext).permissionController,
                allRecordTypes = allRecordTypes,
            )
        }
    }

    val permissionsViewModelFactory: PermissionsViewModelFactory by lazy {
        PermissionsViewModelFactory(coordinator)
    }

    val permissionNav: PermissionNavigationEntryProvider by lazy {
        PermissionNavigationEntryProviderImpl()
    }
}
