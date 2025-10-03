package com.example.healthconnect.ui.screen.record.metadata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.record.component.SelectorComponent
import com.example.healthconnect.ui.screen.record.metadata.DeviceEditorViewModel.Event

@Composable
fun DeviceEditorComponent(
    specifiedDeviceModel: DeviceComponentViewModel.DeviceModel.Specified,
    onDeviceModelChange: (DeviceComponentViewModel.DeviceModel.Specified) -> Unit,
    modifier: Modifier = Modifier,
    deviceTypeMapper: DeviceTypeMapper = Di.deviceTypeMapper,
    viewModel: DeviceEditorViewModel = viewModel(
        modelClass = DeviceEditorViewModel::class.java,
        factory = Di.editorViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(DeviceEditorViewModel.SPECIFIED_DEVICE_KEY, specifiedDeviceModel)
        }
    ),
) {

    LaunchedEffect(viewModel.specifiedDeviceModel) {
        onDeviceModelChange(viewModel.specifiedDeviceModel)
    }

    Column(
        modifier = modifier
    ) {

        SelectorComponent(
            title = "Type",
            supportText = "Client supplied type of the device",
            selectedText = deviceTypeMapper.map(viewModel.specifiedDeviceModel.type),
            items = deviceTypeMapper.deviceTypes,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = { (type, _) ->
                viewModel.onEvent(Event.OnTypeSelected(type))
            }
        )

        OutlinedTextField(
            value = viewModel.specifiedDeviceModel.manufacturer,
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
            value = viewModel.specifiedDeviceModel.model,
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
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceEditorScreenPreview() {
    val sampleDevice = DeviceComponentViewModel.DeviceModel.Specified(
        type = 2,
        manufacturer = "Sample Manufacturer",
        model = "Sample Model"
    )

    DeviceEditorComponent(
        specifiedDeviceModel = sampleDevice,
        onDeviceModelChange = {}
    )
}

@Composable
@Preview(showBackground = true)
fun EmptyDeviceEditorScreenPreview() {
    val sampleDevice = DeviceComponentViewModel.DeviceModel.Specified(
        type = 0,
        manufacturer = "",
        model = ""
    )

    DeviceEditorComponent(
        specifiedDeviceModel = sampleDevice,
        onDeviceModelChange = {}
    )
}