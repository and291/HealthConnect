package com.example.healthconnect.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.theme.HealthConnectTheme

@Composable
fun SdkPermissionsScreen(
    modifier: Modifier = Modifier,
    viewModel: SdkPermissionsViewModel = viewModel(
        modelClass = SdkPermissionsViewModel::class.java,
        factory = Di.parameterlessViewModelFactory,
    ),
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text("Granted Permissions:")
        if (viewModel.isLoading) {
            CircularProgressIndicator(Modifier.size(64.dp))
        } else {
            val sb = StringBuilder()
            for (p in viewModel.grantedPermissions) {
                sb.append("$p; ")
            }
            Text(sb.toString())
            Button(onClick = {
                viewModel.updateGrantedPermissionsSet()
            }) {
                Text("Refresh")
            }
        }
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 720, showBackground = true)
fun SdkPermissionsScreenPreview() {
    Di.applicationContext = LocalContext.current.applicationContext

    HealthConnectTheme {
        SdkPermissionsScreen()
    }
}
