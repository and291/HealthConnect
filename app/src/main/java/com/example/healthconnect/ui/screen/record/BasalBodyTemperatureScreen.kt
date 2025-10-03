package com.example.healthconnect.ui.screen.record

import android.util.Log
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
import com.example.healthconnect.ui.screen.record.metadata.MetadataMapper

@Composable
fun BasalBodyTemperatureScreen(
    record: Record,
    modifier: Modifier = Modifier,
    metadataMapper: MetadataMapper = Di.metadataMapper,
    deviceMapper: DeviceMapper = Di.deviceMapper,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Metadata:")
        MetadataEditorComponent(
            metadataModel = metadataMapper.toUiModel(record.metadata),
            onMetaModelChange = {
                Log.d(this::class.simpleName, "Metadata: $it")
            },
        )

        DeviceComponent(
            deviceModel = deviceMapper.toUiModel(record.metadata.device),
            onDeviceModelChange = {
                Log.d(this::class.simpleName, "Device: $it")
            },
        )
    }
}