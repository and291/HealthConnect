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
import androidx.health.connect.client.records.metadata.Device
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.record.metadata.DeviceEditorViewModel.Event
import com.example.healthconnect.ui.screen.record.component.SelectorComponent

@Composable
fun DeviceEditorScreen(
    device: Device,
    onDeviceChange: (Device) -> Unit,
    modifier: Modifier = Modifier,
    deviceMapper: DeviceMapper = Di.deviceMapper,
    viewModel: DeviceEditorViewModel = viewModel(
        modelClass = DeviceEditorViewModel::class.java,
        factory = Di.editorViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(DeviceEditorViewModel.DEVICE_KEY, deviceMapper.toState(device))
        }
    ),
    deviceTypeMapper: DeviceTypeMapper = Di.deviceTypeMapper,
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
            selectedText = deviceTypeMapper.map(viewModel.state.type),
            items = deviceTypeMapper.deviceTypes,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = { (type, name) ->
                viewModel.onEvent(
                    Event.OnTypeSelected(
                    type = type,
                    name = name,
                ))
            }
        )

        OutlinedTextField(
            value = viewModel.state.manufacturer,
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
            value = viewModel.state.model,
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
            onDeviceChange(deviceMapper.toDevice(viewModel.state))
        }) {
            Text("Save")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceEditorScreenPreview() {
    val sampleDevice = Device(
        type = Device.TYPE_PHONE,
        manufacturer = "Sample Manufacturer",
        model = "Sample Model"
    )

    DeviceEditorScreen(
        device = sampleDevice,
        onDeviceChange = {}
    )
}

@Composable
@Preview(showBackground = true)
fun EmptyDeviceEditorScreenPreview() {
    val sampleDevice = Device(
        type = Device.TYPE_UNKNOWN,
    )

    DeviceEditorScreen(
        device = sampleDevice,
        onDeviceChange = {}
    )
}