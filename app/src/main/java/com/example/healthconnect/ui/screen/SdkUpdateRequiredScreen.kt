package com.example.healthconnect.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.ui.theme.HealthConnectTheme

@Composable
fun SdkUpdateRequiredScreen(
    startUpdateActivity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Health Connect SDK update required")
        Button(
            content = { Text(text = "Update") },
            onClick = { startUpdateActivity() },
        )
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 720, showBackground = true)
fun SdkUpdateRequiredScreenPreview() = HealthConnectTheme {
    SdkUpdateRequiredScreen(
        startUpdateActivity = {}
    )
}
