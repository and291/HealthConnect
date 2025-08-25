package com.example.healthconnect.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.records.Record
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.InsertRecordScreen
import com.example.healthconnect.ui.screen.RecordsScreen
import com.example.healthconnect.ui.screen.SdkAvailableScreen
import com.example.healthconnect.ui.screen.SdkUnavailableScreen
import com.example.healthconnect.ui.screen.SdkUpdateRequiredScreen
import com.example.healthconnect.ui.theme.HealthConnectTheme
import kotlin.reflect.KClass

// Define keys that will identify content
data object Splash
data object Available
data object Unavailable
data object ProviderUpdateRequired
data class Records(val recordType: KClass<Record>)
data class Insert(val recordType: KClass<Record>)

class MainActivity : ComponentActivity() {

    private lateinit var activityViewModel: ActivityViewModel

    private val requestPermissionActivityContract =
        PermissionController.createRequestPermissionResultContract()
    private val permissionResult = registerForActivityResult(requestPermissionActivityContract) {

    }
    private val requestPermission: (String) -> Unit = { sdkPermission ->
        permissionResult.launch(setOf(sdkPermission))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO consider moving DI initialization into Application.onCreate() or other entry point
        Di.isPreview = false
        Di.applicationContext = this.application
        //injects for current activity below
        activityViewModel = Di.parameterlessViewModelFactory.create(ActivityViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            // Create a back stack, specifying the key the app should start with
            val backStack = remember { mutableStateListOf<Any>(Splash) }

            HealthConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                    ) { key ->
                        when (key) {
                            is Available -> NavEntry(key) {
                                SdkAvailableScreen(
                                    onTypeClick = { type ->
                                        backStack.add(Records(type))
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }

                            is Unavailable -> NavEntry(key) {
                                SdkUnavailableScreen(
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }

                            is ProviderUpdateRequired -> NavEntry(key) {
                                SdkUpdateRequiredScreen(
                                    startUpdateActivity = { intent ->
                                        startActivity(
                                            Intent.createChooser(
                                                intent,
                                                "Choose app to update Health Connect library"
                                            )
                                        )
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }

                            is Records -> NavEntry(key) {
                                RecordsScreen(
                                    requestPermission = requestPermission,
                                    onInsertRecordClick = { backStack.add(Insert(key.recordType)) },
                                    recordType = key.recordType,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }

                            is Insert -> NavEntry(key) {
                                InsertRecordScreen(
                                    recordType = key.recordType,
                                    modifier = Modifier.padding(innerPadding),
                                )
                            }

                            else -> NavEntry(Unit) { Text("Unknown route") }
                        }
                    }
                }
            }

            val status by activityViewModel.sdkStatus
            val destination = when (status) {
                SDK_AVAILABLE -> Available
                SDK_UNAVAILABLE -> Unavailable
                SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> ProviderUpdateRequired
                else -> TODO()
            }
            backStack.apply {
                clear()
                add(destination)
            }
        }
    }
}
