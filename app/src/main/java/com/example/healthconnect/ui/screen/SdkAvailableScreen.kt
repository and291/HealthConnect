package com.example.healthconnect.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.component.SdkPermissionsComponent
import com.example.healthconnect.ui.screen.SdkAvailableViewModel.Effect.RequestSinglePermission
import com.example.healthconnect.ui.theme.HealthConnectTheme

@Composable
fun SdkAvailableScreen(
    requestPermission: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SdkAvailableViewModel = viewModel(
        modelClass = SdkAvailableViewModel::class.java,
        factory = Di.parameterlessViewModelFactory
    ),
) {

    val effect by viewModel.effect.collectAsState(null)

    LaunchedEffect(effect) {
        effect?.let { modification ->
            when (modification) {
                is RequestSinglePermission -> requestPermission(modification.sdkPermission)
                is SdkAvailableViewModel.Effect.StepsInsertedSuccessfully -> {
                    //ok
                }
            }
            viewModel.effectConsumed(modification)
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(text = "Health Connect SDK is Available")

        SdkPermissionsComponent()

        Button(onClick = {
            viewModel.insertSteps()
        }) {
            Text(text = "Insert 120 steps for the last 2 minutes")
        }
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 720, showBackground = true)
fun SdkAvailableScreenPreview() {
    Di.applicationContext = LocalContext.current.applicationContext

    HealthConnectTheme {
        SdkAvailableScreen(
            requestPermission = {}
        )
    }
}
