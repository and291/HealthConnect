package com.example.healthconnect.editor.api.ui.mapper

import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.ui.model.DeviceEditorModel

class DeviceMapper {

    fun toEntity(device: Device?): DeviceEditorModel =
        if (device != null) {
            DeviceEditorModel.Specified(
                type = device.type,
                manufacturer = device.manufacturer ?: "",
                model = device.model ?: "",
            )
        } else {
            DeviceEditorModel.Empty
        }

    fun toLibDevice(deviceEditorModel: DeviceEditorModel): Device? =
        when (deviceEditorModel) {
            DeviceEditorModel.Empty -> null
            is DeviceEditorModel.Specified -> Device(
                type = deviceEditorModel.type,
                manufacturer = deviceEditorModel.manufacturer.ifBlank { null },
                model = deviceEditorModel.model.ifBlank { null }
            )
        }
}