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
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
import androidx.health.connect.client.PermissionController
import com.example.healthconnect.di.Di
import com.example.healthconnect.navigation.api.NavigationEntry
import com.example.healthconnect.ui.navigation.AppNavigationEntry
import com.example.healthconnect.ui.navigation.CreateNavDisplay
import com.example.healthconnect.ui.theme.HealthConnectTheme

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
        Di.also {
            it.isPreview = false
            it.applicationContext = this.application
        }
        com.example.healthconnect.utilty.impl.di.Di.also {
            it.isPreview = false
            it.applicationContext = this.application
        }
        com.example.healthconnect.editor.impl.di.Di.also {
            it.componentProvider = com.example.healthconnect.components.impl.di.Di.componentProvider
            it.update = com.example.healthconnect.utilty.impl.di.Di.update
            it.insert = com.example.healthconnect.utilty.impl.di.Di.insert
        }

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
                        requestPermission = requestPermission,
                        activity = this,
                    )
                }
            }

            val status by activityViewModel.sdkStatus
            val destination = when (status) {
                SDK_AVAILABLE -> AppNavigationEntry.Available
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
