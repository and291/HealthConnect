package com.example.healthconnect.ui.screen.record.metadata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.record.metadata.DeviceComponentViewModel.DeviceModel
import com.example.healthconnect.ui.screen.record.metadata.DeviceComponentViewModel.Event

@Composable
fun DeviceComponent(
    deviceModel: DeviceModel,
    modifier: Modifier = Modifier,
    viewModel: DeviceComponentViewModel = viewModel(
        modelClass = DeviceComponentViewModel::class,
        factory = Di.editorViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(DeviceComponentViewModel.DEVICE_KEY, deviceModel)
        }
    )
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (val device = viewModel.deviceModel) {
            DeviceModel.Empty -> {
                Text("Device is not specified")
                Button(onClick = {
                    viewModel.onEvent(Event.OnSpecifyDevice)
                }) {
                    Text("Specify")
                }
            }

            is DeviceModel.Specified -> {
                DeviceEditorComponent(
                    specifiedDeviceModel = device,
                    onDeviceModelChange = {},
                    modifier = modifier
                )
                Button(onClick = {
                    viewModel.onEvent(Event.OnRemoveDevice)
                }) {
                    Text("Remove device")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceComponentPreview() {
    // Preview for the empty state of DeviceComponent
    val emptyDeviceModel = DeviceModel.Empty

    DeviceComponent(
        deviceModel = emptyDeviceModel,
        modifier = Modifier
    )
}

@Composable
@Preview(showBackground = true)
fun SpecifiedDeviceComponentPreview() {
    // Example preview for the specified state of DeviceComponent
    val specifiedDeviceModel = DeviceModel.Specified(
        type = 1, // Example device type
        manufacturer = "Example Manufacturer",
        model = "Example Model"
    )

    DeviceComponent(
        deviceModel = specifiedDeviceModel,
        modifier = Modifier
    )
}