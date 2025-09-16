package com.example.healthconnect.ui.screen.record.metadata

import androidx.health.connect.client.records.metadata.Device

class DeviceMapper {

    fun toState(device: Device): DeviceEditorViewModel.State = DeviceEditorViewModel.State(
        type = device.type,
        manufacturer = device.manufacturer ?: "",
        model = device.model ?: "",
    )

    fun toDevice(state: DeviceEditorViewModel.State): Device = Device(
        type = state.type,
        manufacturer = state.manufacturer.ifBlank { null },
        model = state.model.ifBlank { null }
    )
}