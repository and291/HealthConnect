package com.example.healthconnect.ui.screen.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.Record
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.record.metadata.DeviceComponent
import com.example.healthconnect.ui.screen.record.metadata.DeviceMapper
import com.example.healthconnect.ui.screen.record.metadata.MetadataEditorComponent
import com.example.healthconnect.ui.screen.record.metadata.MetadataEditorViewModel
import com.example.healthconnect.ui.screen.record.metadata.MetadataMapper

@Composable
fun BasalBodyTemperatureScreen(
    record: Record,
    onMetadataClick: (MetadataEditorViewModel.MetadataModel) -> Unit,
    modifier: Modifier = Modifier,
    metadataMapper: MetadataMapper = Di.metadataMapper,
    deviceMapper: DeviceMapper = Di.deviceMapper,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "FFFFuuuuuuuu",
            modifier = Modifier
                .clickable {
                    val metadataUiModel =
                        metadataMapper.toUiModel(record.metadata)
                    onMetadataClick(metadataUiModel)
                }
        )
        MetadataEditorComponent(
            metadataModel = metadataMapper.toUiModel(record.metadata),
            onMetaModelChange = {},
        )

        DeviceComponent(
            deviceModel = deviceMapper.toUiModel(record.metadata.device),
            //onDeviceModelChange = {},
        )
    }
}