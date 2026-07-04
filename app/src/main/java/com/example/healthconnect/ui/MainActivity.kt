package com.example.healthconnect.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
import com.example.healthconnect.di.Di
import com.example.healthconnect.editor.api.di.EditorFeatureScope
import com.example.healthconnect.integration.editor.ComponentFactoryImpl
import com.example.healthconnect.integration.editor.CreateEditableImpl
import com.example.healthconnect.integration.editor.EditableFactoryImpl
import com.example.healthconnect.integration.editor.GetEditableImpl
import com.example.healthconnect.integration.editor.InsertImpl
import com.example.healthconnect.integration.editor.UpdateImpl
import com.example.healthconnect.integration.permission_overview.data.PermissionEntryMapper
import com.example.healthconnect.integration.permission_overview.data.PermissionResolverImpl
import com.example.healthconnect.integration.permission_overview.ui.PermissionContractProviderImpl
import com.example.healthconnect.integration.record_list.DeleteRecordImpl
import com.example.healthconnect.integration.record_list.LoadRecordsImpl
import com.example.healthconnect.integration.record_list.RecordSummaryFactoryImpl
import com.example.healthconnect.navigation.api.NavigationEntry
import com.example.healthconnect.permission_overview.api.di.PermissionOverviewFeatureScope
import com.example.healthconnect.record_list.api.di.RecordListFeatureScope
import com.example.healthconnect.ui.navigation.AppNavigationEntry
import com.example.healthconnect.ui.navigation.CreateNavDisplay
import com.example.healthconnect.ui.theme.HealthConnectTheme
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntry

class MainActivity : ComponentActivity() {

    private lateinit var activityViewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO consider moving DI initialization into Application.onCreate() or other entry point
        Di.also {
            it.isPreview = false
            it.applicationContext = this.application
        }
        com.example.healthconnect.utilty.impl.di.Di.also {
            it.isPreview = false
            it.applicationContext = this.application
        }
        val permissionOverviewScope = PermissionOverviewFeatureScope(
            application = application,
            permissions = PermissionEntryMapper().map(com.example.healthconnect.utilty.impl.di.Di.permissionRepository.libraryPermissions()),
            permissionContractProvider = PermissionContractProviderImpl(),
            resolver = PermissionResolverImpl(HealthConnectClient.getOrCreate(applicationContext).permissionController) //TODO handle errors. Rethink class instantiation
        ).apply { init() }

        val editorFeatureScope = EditorFeatureScope(
            getEditable = GetEditableImpl(
                modelFactory = com.example.healthconnect.utilty.impl.di.Di.modelFactory,
            ),
            update = UpdateImpl(),
            componentFactory = ComponentFactoryImpl(),
            editableFactory = EditableFactoryImpl(),
            createEditable = CreateEditableImpl(),
            insert = InsertImpl()
        ).apply { init() }

        val recordListFeatureScope = RecordListFeatureScope(
            loadRecords = LoadRecordsImpl(com.example.healthconnect.utilty.impl.di.Di.readAll),
            deleteRecord = DeleteRecordImpl(com.example.healthconnect.utilty.impl.di.Di.delete),
            summaryFactory = RecordSummaryFactoryImpl(),
        ).apply { init() }

        //injects for current activity below
        activityViewModel = Di.parameterlessViewModelFactory.create(ActivityViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            // Create a back stack, specifying the key the app should start with
            val backStack = remember { mutableStateListOf<NavigationEntry>(AppNavigationEntry.Splash) }

            HealthConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CreateNavDisplay(
                        backStack = backStack,
                        innerPadding = innerPadding,
                        activity = this,
                        libraryNavigation = Di.libraryNavigation,
                    )
                }
            }

            val status by activityViewModel.sdkStatus
            val destination = when (status) {
                SDK_AVAILABLE -> UtilityNavigationEntry.Dashboard
                SDK_UNAVAILABLE -> AppNavigationEntry.Unavailable
                SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> AppNavigationEntry.ProviderUpdateRequired
                else -> TODO()
            }
            backStack.apply {
                clear()
                add(destination)
            }
        }
    }
}
