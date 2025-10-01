package com.example.healthconnect.ui.screen.record.metadata

import androidx.health.connect.client.records.metadata.Device

class DeviceMapper {

    fun toUiModel(device: Device): DeviceEditorViewModel.DeviceModel = DeviceEditorViewModel.DeviceModel(
        type = device.type,
        manufacturer = device.manufacturer ?: "",
        model = device.model ?: "",
    )

    fun toDevice(deviceModel: DeviceEditorViewModel.DeviceModel): Device = Device(
        type = deviceModel.type,
        manufacturer = deviceModel.manufacturer.ifBlank { null },
        model = deviceModel.model.ifBlank { null }
    )
}