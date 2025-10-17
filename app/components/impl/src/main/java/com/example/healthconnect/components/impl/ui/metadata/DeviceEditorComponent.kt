package com.example.healthconnect.components.impl.ui.metadata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.api.ui.model.DeviceEditorModel
import com.example.healthconnect.components.impl.ui.SelectorComponent
import com.example.healthconnect.components.impl.ui.metadata.mapper.DeviceTypeMapper

@Composable
fun DeviceEditorComponent(
    specifiedDeviceEditorModel: DeviceEditorModel.Specified,
    onTypeItemSelected: (Pair<Int, String>) -> Unit,
    onManufacturerValueChanged: (String) -> Unit,
    onModelValueChanged: (String) -> Unit,
    onRemoveDeviceClicked: () -> Unit,
    modifier: Modifier = Modifier,
    deviceTypeMapper: DeviceTypeMapper = Di.deviceTypeMapper,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {

        SelectorComponent(
            title = "Type",
            supportText = "Client supplied type of the device",
            selectedText = deviceTypeMapper.mapName(specifiedDeviceEditorModel.type),
            items = deviceTypeMapper.deviceTypes,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = onTypeItemSelected,
        )

        OutlinedTextField(
            value = specifiedDeviceEditorModel.manufacturer,
            enabled = true,
            singleLine = true,
            onValueChange = onManufacturerValueChanged,
            label = {
                Text("Manufacturer")
            },
            supportingText = {
                Text("Optional client supplied manufacturer of the device")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = specifiedDeviceEditorModel.model,
            enabled = true,
            singleLine = true,
            onValueChange = onModelValueChanged,
            label = {
                Text("Model")
            },
            supportingText = {
                Text("Optional client supplied model of the device")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = onRemoveDeviceClicked) {
            Text("Remove device")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceEditorScreenPreview() {
    val sampleDevice = DeviceEditorModel.Specified(
        type = 2,
        manufacturer = "Sample Manufacturer",
        model = "Sample Model"
    )

    DeviceEditorComponent(
        specifiedDeviceEditorModel = sampleDevice,
        onTypeItemSelected = { },
        onManufacturerValueChanged = { },
        onModelValueChanged = { },
        onRemoveDeviceClicked = { },
    )
}

@Composable
@Preview(showBackground = true)
fun EmptyDeviceEditorScreenPreview() {
    val sampleDevice = DeviceEditorModel.Specified(
        type = 0,
        manufacturer = "",
        model = ""
    )

    DeviceEditorComponent(
        specifiedDeviceEditorModel = sampleDevice,
        onTypeItemSelected = { },
        onManufacturerValueChanged = { },
        onModelValueChanged = { },
        onRemoveDeviceClicked = { },
    )
}