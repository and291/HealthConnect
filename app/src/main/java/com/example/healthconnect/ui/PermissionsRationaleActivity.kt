package com.example.healthconnect.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.healthconnect.ui.theme.HealthConnectTheme

class PermissionsRationaleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            HealthConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PermissionRationaleScreen(
                        onLearnMore = {
                            val healthConnectGuideUrl =
                                "https://developer.android.com/health-and-fitness/guides/health-connect"
                            val intent =
                                Intent(Intent.ACTION_VIEW, healthConnectGuideUrl.toUri())
                            startActivity(intent)
                        },
                        onDismiss = {
                            this.finish()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionRationaleScreen(
    onLearnMore: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Permission Rationale for HealthConnect Test Utility",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = """
                The HealthConnect Test Utility is designed to evaluate the capabilities and current state of the Android Health Connect library. To perform these tests effectively, the app requests permissions to access your device's health and fitness data. Below is a detailed explanation:
                
                1. Access Health Data: The app needs permission to read and write health and fitness data to validate the functionality of the Health Connect library.
                2. Write Health Data: The app may create test entries to simulate real-world use cases and verify data handling.
                3. Read Health Data: The app reviews existing health data to ensure compatibility and accuracy.
                4. Activity and Sensor Data: Permissions are required to test features involving sensor or activity data.
                
                Privacy Assurance:
                The HealthConnect Test Utility does not store, share, or use your personal health data beyond the scope of testing. All data interactions remain local to your device.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Learn more about the Health Connect library here.",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.clickable {
                onLearnMore()
            },
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onDismiss) {
            Text("Got it")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PermissionRationaleScreenPreview() {
    HealthConnectTheme {
        PermissionRationaleScreen({}, {})
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
fun PermissionRationaleScreenPreviewNarrow() {
    HealthConnectTheme {
        PermissionRationaleScreen({}, {})
    }
}
