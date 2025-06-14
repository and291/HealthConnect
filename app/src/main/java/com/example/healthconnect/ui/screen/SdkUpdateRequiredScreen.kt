package com.example.healthconnect.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.theme.HealthConnectTheme

@Composable
fun SdkUpdateRequiredScreen(
    startUpdateActivity: (Intent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SdkUpdateRequiredViewModel = viewModel(
        modelClass = SdkUpdateRequiredViewModel::class.java,
        factory = Di.parameterlessViewModelFactory,
    ),
) {
    Column(
        modifier = modifier
    ) {
        Text(text = "Health Connect SDK update required")
        Button(onClick = {
            val intent = viewModel.getUpdateIntent()
            startUpdateActivity(intent)
        }) {
            Text(text = "Update")
        }
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 720, showBackground = true)
fun SdkUpdateRequiredScreenPreview() {
    HealthConnectTheme {
        SdkUpdateRequiredScreen(
            startUpdateActivity = {}
        )
    }
}
