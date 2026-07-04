package com.example.healthconnect.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.healthconnect.dashboard.api.navigation.DashboardNavigationEntryProvider
import com.example.healthconnect.dashboard.api.navigation.DashboardNavigationEntryProviderImpl
import com.example.healthconnect.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntry
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntryProvider
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntryProviderImpl
import com.example.healthconnect.permission_overview.api.navigation.PermissionNavigationEntry
import com.example.healthconnect.permission_overview.api.navigation.PermissionNavigationEntryProvider
import com.example.healthconnect.permission_overview.api.navigation.PermissionNavigationEntryProviderImpl
import com.example.healthconnect.record_list.api.domain.entity.RecordModel
import com.example.healthconnect.record_list.api.navigation.RecordListNavigationEntry
import com.example.healthconnect.record_list.api.navigation.RecordListNavigationEntryProvider
import com.example.healthconnect.record_list.api.navigation.RecordListNavigationEntryProviderImpl
import com.example.healthconnect.ui.ParameterlessViewModelFactory
import com.example.healthconnect.ui.navigation.LibraryNavigation
import kotlin.reflect.KClass

object Di { //move to dagger. keep all features
    var isPreview = true

    lateinit var applicationContext: Context

    private val libraryRepository by lazy {
        if (isPreview) {
            object : LibraryRepository {
                override fun getSdkStatus(): Int {
                    return HealthConnectClient.SDK_AVAILABLE
                }

                override suspend fun getGrantedPermissions(): Set<String> {
                    return setOf("sdk:permission")
                }
            }
        } else {
            LibraryRepositoryImpl(applicationContext)
        }
    }

    val parameterlessViewModelFactory by lazy {
        ParameterlessViewModelFactory(libraryRepository)
    }

    val libraryNavigation: LibraryNavigation by lazy {
        LibraryNavigation(applicationContext)
    }

    val dashboardNav: DashboardNavigationEntryProvider = DashboardNavigationEntryProviderImpl(
        permissionOverview = PermissionNavigationEntry.Overview,
        getRecordsEntry = { type: KClass<*>, titleRes: Int ->
            RecordListNavigationEntry.List(recordType = type, titleRes = titleRes)
        }
    )
    val editorNav: EditorNavigationEntryProvider = EditorNavigationEntryProviderImpl()
    val permissionNav: PermissionNavigationEntryProvider = PermissionNavigationEntryProviderImpl()
    val recordListNav: RecordListNavigationEntryProvider = RecordListNavigationEntryProviderImpl(
        getEditEntry = { record: RecordModel ->
            val model = com.example.healthconnect.integration.record_list.unwrap(record)
            EditorNavigationEntry.EditRecordScreen(
                model = com.example.healthconnect.integration.editor.wrapModel(model),
                recordClass = com.example.healthconnect.utilty.impl.di.Di.modelFactory.createByModel(model)::class,
            )

            //TODO get record klass from params
        },
        getInsertEntry = { klass: KClass<*> -> EditorNavigationEntry.Insert(klass) }
    )
}
