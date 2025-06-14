package com.example.healthconnect.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
import androidx.health.connect.client.PermissionController
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.SdkAvailableScreen
import com.example.healthconnect.ui.screen.SdkUnavailableScreen
import com.example.healthconnect.ui.screen.SdkUpdateRequiredScreen
import com.example.healthconnect.ui.theme.HealthConnectTheme

class MainActivity : ComponentActivity() {

    private lateinit var activityViewModel: ActivityViewModel

    private val requestPermissionActivityContract =
        PermissionController.createRequestPermissionResultContract()
    private val permissionResult = registerForActivityResult(requestPermissionActivityContract) {

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
            HealthConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val status by activityViewModel.sdkStatus
                    when (status) {
                        SDK_AVAILABLE -> SdkAvailableScreen(
                            requestPermission = { sdkPermission ->
                                permissionResult.launch(setOf(sdkPermission))
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        SDK_UNAVAILABLE -> SdkUnavailableScreen(
                            modifier = Modifier.padding(innerPadding)
                        )

                        SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> SdkUpdateRequiredScreen(
                            startUpdateActivity = { intent ->
                                startActivity(
                                    Intent.createChooser(
                                        intent,
                                        "Chose app to update Health Connect library"
                                    )
                                )
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
