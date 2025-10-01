package com.example.healthconnect.ui.screen.record.metadata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.record.component.SelectorComponent
import com.example.healthconnect.ui.screen.record.metadata.DeviceEditorViewModel.DeviceModel
import com.example.healthconnect.ui.screen.record.metadata.DeviceEditorViewModel.Event

@Composable
fun DeviceEditorScreen(
    deviceModel: DeviceModel,
    onDeviceModelChange: (DeviceModel) -> Unit,
    modifier: Modifier = Modifier,
    deviceTypeMapper: DeviceTypeMapper = Di.deviceTypeMapper,
    viewModel: DeviceEditorViewModel = viewModel(
        modelClass = DeviceEditorViewModel::class.java,
        factory = Di.editorViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(DeviceEditorViewModel.DEVICE_KEY, deviceModel)
        }
    ),
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        SelectorComponent(
            title = "Type",
            supportText = "Client supplied type of the device",
            selectedText = deviceTypeMapper.map(viewModel.deviceModel.type),
            items = deviceTypeMapper.deviceTypes,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = { (type, _) ->
                viewModel.onEvent(Event.OnTypeSelected(type))
            }
        )

        OutlinedTextField(
            value = viewModel.deviceModel.manufacturer,
            enabled = true,
            singleLine = true,
            onValueChange = { value ->
                viewModel.onEvent(Event.OnManufacturerChanged(value))
            },
            label = {
                Text("Manufacturer")
            },
            supportingText = {
                Text("Optional client supplied manufacturer of the device")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.deviceModel.model,
            enabled = true,
            singleLine = true,
            onValueChange = { value ->
                viewModel.onEvent(Event.OnModelChanged(value))
            },
            label = {
                Text("Model")
            },
            supportingText = {
                Text("Optional client supplied model of the device")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            onDeviceModelChange(viewModel.deviceModel)
        }) {
            Text("Save")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceEditorScreenPreview() {
    val sampleDevice = DeviceModel(
        type = 2,
        manufacturer = "Sample Manufacturer",
        model = "Sample Model"
    )

    DeviceEditorScreen(
        deviceModel = sampleDevice,
        onDeviceModelChange = {}
    )
}

@Composable
@Preview(showBackground = true)
fun EmptyDeviceEditorScreenPreview() {
    val sampleDevice = DeviceModel(
        type = 0,
    )

    DeviceEditorScreen(
        deviceModel = sampleDevice,
        onDeviceModelChange = {}
    )
}