package com.example.healthconnect.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.healthconnect.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntryProvider
import com.example.healthconnect.editor.impl.navigation.EditorNavigationEntryProviderImpl
import com.example.healthconnect.ui.ParameterlessViewModelFactory
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntryProvider
import com.example.healthconnect.utilty.impl.navigation.UtilityNavigationEntryProviderImpl

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
        ParameterlessViewModelFactory(applicationContext, libraryRepository)
    }

    val utilityNav: UtilityNavigationEntryProvider = UtilityNavigationEntryProviderImpl()
    val editorNav: EditorNavigationEntryProvider = EditorNavigationEntryProviderImpl()
}
