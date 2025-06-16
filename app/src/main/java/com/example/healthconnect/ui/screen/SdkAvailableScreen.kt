package com.example.healthconnect.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = modifier.padding(16.dp)
    ) {
        Text(text = "Health Connect SDK is Available")

        SdkPermissionsComponent()

        Button(onClick = {
            viewModel.insertSteps()
        }) {
            Text(text = "Insert 120 steps for the last 2 minutes")
        }

        when (val s = viewModel.state) {
            is SdkAvailableViewModel.State.RecordTypes -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(s.availableTypes) { type ->
                        Text(
                            text = type.simpleName.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clickable {

                                }
                        )
                    }
                }
            }
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
