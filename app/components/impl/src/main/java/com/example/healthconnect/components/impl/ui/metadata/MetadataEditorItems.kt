package com.example.healthconnect.components.impl.ui.metadata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.ui.model.DeviceComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.StringComponentModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.SelectorComponent
import com.example.healthconnect.components.impl.ui.StringEditorComponent
import com.example.healthconnect.components.impl.ui.metadata.mapper.RecordingMethodMapper
import java.time.Instant

/**
 * Extension for LazyListScope to include Metadata editor items.
 * This avoids nested LazyLists while keeping metadata fields together.
 */
fun LazyListScope.metadataEditorItems(
    model: MetadataComponentModel,
    onChanged: (MetadataComponentModel) -> Unit,
    recordingMethodMapper: RecordingMethodMapper = Di.recordingMethodMapper
) {
    item(key = "metadata_header_${model.id}") {
        Text(
            text = "Metadata",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }

    item(key = "metadata_recording_${model.id}") {
        SelectorComponent(
            title = "Recording Method",
            supportText = "Client supplied data recording method to help to understand how the data was recorded",
            selectedText = recordingMethodMapper.map(model.recordingMethod),
            items = recordingMethodMapper.recordingMethods,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = { (type, _) ->
                onChanged(model.copy(recordingMethod = type))
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    item(key = "metadata_id_${model.id}") {
        StringEditorComponent(
            model = StringComponentModel(
                value = model.id,
                type = StringComponentModel.Type.MetadataId(),
                readOnly = true
            ),
            onChanged = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    item(key = "metadata_origin_${model.id}") {
        StringEditorComponent(
            model = StringComponentModel(
                value = model.dataOriginPackageName,
                type = StringComponentModel.Type.MetadataDataOrigin(),
                readOnly = true
            ),
            onChanged = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    item(key = "metadata_modified_${model.id}") {
        StringEditorComponent(
            model = StringComponentModel(
                value = model.lastModifiedTime.toString(),
                type = StringComponentModel.Type.MetadataLastModifiedTime(),
                readOnly = true
            ),
            onChanged = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    item(key = "metadata_client_record_id_${model.id}") {
        StringEditorComponent(
            model = StringComponentModel(
                value = model.clientRecordId,
                type = StringComponentModel.Type.MetadataClientRecordId()
            ),
            onChanged = {
                onChanged(model.copy(clientRecordId = it.value))
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    item(key = "metadata_client_record_version_${model.id}") {
        StringEditorComponent(
            model = StringComponentModel(
                value = model.clientRecordVersion,
                type = StringComponentModel.Type.MetadataClientRecordVersion()
            ),
            onChanged = {
                onChanged(model.copy(clientRecordVersion = it.value))
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    item(key = "metadata_device_${model.id}") {
        when (val deviceComponentModel = model.deviceComponentModel) {
            DeviceComponentModel.Empty -> Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Device is not specified")
                Button(onClick = {
                    onChanged(
                        model.copy(
                            deviceComponentModel = DeviceComponentModel.Specified(
                                type = Device.TYPE_UNKNOWN,
                                manufacturer = "",
                                model = ""
                            )
                        )
                    )
                }) {
                    Text("Specify")
                }
            }

            is DeviceComponentModel.Specified -> DeviceEditorComponent(
                specifiedDeviceComponentModel = deviceComponentModel,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                onTypeItemSelected = { (type, _) ->
                    onChanged(
                        model.copy(
                            deviceComponentModel = deviceComponentModel.copy(type = type)
                        )
                    )
                },
                onManufacturerValueChanged = {
                    onChanged(
                        model.copy(
                            deviceComponentModel = deviceComponentModel.copy(manufacturer = it)
                        )
                    )
                },
                onModelValueChanged = {
                    onChanged(
                        model.copy(
                            deviceComponentModel = deviceComponentModel.copy(model = it)
                        )
                    )
                },
                onRemoveDeviceClicked = {
                    onChanged(model.copy(deviceComponentModel = DeviceComponentModel.Empty))
                },
            )
        }
    }

    item(key = "metadata_divider_${model.id}") {
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
@Preview(showBackground = true)
fun MetadataEditorItemsPreview() {
    val model = MetadataComponentModel(
        recordingMethod = 1,
        id = "sample-id",
        dataOriginPackageName = "com.example.app",
        lastModifiedTime = Instant.now(),
        clientRecordId = "client-id",
        clientRecordVersion = "1",
        deviceComponentModel = DeviceComponentModel.Empty
    )
    LazyColumn {
        metadataEditorItems(
            model = model,
            onChanged = {}
        )
    }
}
