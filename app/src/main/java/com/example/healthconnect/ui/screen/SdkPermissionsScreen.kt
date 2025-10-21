package com.example.healthconnect.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    LaunchedEffect("") {
        viewModel.update()
    }

    Box(
        modifier = modifier,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            item {
                Text("List of granted Permissions:")
            }
            items(viewModel.grantedPermissions) {
                Text(it)
            }
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp)
                    .size(64.dp)
                    .align(Alignment.Center)
            )
        } else {
            Button(
                onClick = { viewModel.update() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Image(
                    painter = painterResource(android.R.drawable.stat_notify_sync_noanim),
                    contentDescription = "Refresh"
                )
            }
        }
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 300, showBackground = true)
fun SdkPermissionsScreenPreview() {
    Di.applicationContext = LocalContext.current.applicationContext

    HealthConnectTheme {
        SdkPermissionsScreen()
    }
}
