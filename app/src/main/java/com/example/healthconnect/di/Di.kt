package com.example.healthconnect.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.healthconnect.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntry
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntryProvider
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntryProviderImpl
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.permission_overview.api.navigation.PermissionNavigationEntry
import com.example.healthconnect.permission_overview.api.navigation.PermissionNavigationEntryProvider
import com.example.healthconnect.permission_overview.api.navigation.PermissionNavigationEntryProviderImpl
import com.example.healthconnect.ui.ParameterlessViewModelFactory
import com.example.healthconnect.ui.navigation.LibraryNavigation
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntryProvider
import com.example.healthconnect.utilty.impl.navigation.UtilityNavigationEntryProviderImpl
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

    val utilityNav: UtilityNavigationEntryProvider = UtilityNavigationEntryProviderImpl(
        permissionOverview = PermissionNavigationEntry.Overview,
        getEditEntry = { model: Model ->
            EditorNavigationEntry.EditRecordScreen(
                model = com.example.healthconnect.integration.editor.wrapModel(model),
                recordClass = com.example.healthconnect.editor.impl.di.Di.modelFactory.createByModel(model)::class,
            )

            //TODO get record klass from params
        },
        getInsertEntry = { it: KClass<out Model> -> EditorNavigationEntry.Insert(it) }
    )
    val editorNav: EditorNavigationEntryProvider = EditorNavigationEntryProviderImpl()
    val permissionNav: PermissionNavigationEntryProvider = PermissionNavigationEntryProviderImpl()
}
