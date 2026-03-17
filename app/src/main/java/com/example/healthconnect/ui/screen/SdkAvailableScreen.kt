package com.example.healthconnect.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.Record
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.ui.mapper.RecordTypeNameMapper
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
    recordTypeIconMapper: RecordTypeIconMapper = Di.recordTypeIconMapper,
    recordTypeNameMapper: RecordTypeNameMapper = Di.recordTypeNameMapper,
) {

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(text = "Health Connect SDK is Available")

        SdkPermissionsScreen(
            modifier = Modifier
                .background(Color.LightGray)
                .weight(0.25f)
        )

        when (val s = viewModel.state) {
            is SdkAvailableViewModel.State.RecordTypes -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxSize(),
                ) {
                    items(s.availableTypes) { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clickable {
                                    @Suppress("UNCHECKED_CAST")
                                    onTypeClick(type as KClass<Record>)
                                }
                        ) {
                            Icon(
                                imageVector = recordTypeIconMapper.icon(type),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(text = stringResource(recordTypeNameMapper.nameRes(type)))
                        }
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