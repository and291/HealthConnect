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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.Record
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.theme.HealthConnectTheme
import kotlin.reflect.KClass

@Composable
fun SdkAvailableScreen(
    onTypeClick: (KClass<Record>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SdkAvailableViewModel = viewModel(
        modelClass = SdkAvailableViewModel::class.java,
        factory = Di.parameterlessViewModelFactory
    ),
) {

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(text = "Health Connect SDK is Available")

        SdkPermissionsScreen()

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
                                    onTypeClick(type as KClass<Record>)
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
            onTypeClick = {}
        )
    }
}
