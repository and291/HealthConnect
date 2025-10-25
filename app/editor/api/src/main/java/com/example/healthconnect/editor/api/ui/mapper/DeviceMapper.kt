package com.example.healthconnect.editor.api.ui.mapper

import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.ui.model.DeviceComponentModel

class DeviceMapper {

    fun toEntity(device: Device?): DeviceComponentModel =
        if (device != null) {
            DeviceComponentModel.Specified(
                type = device.type,
                manufacturer = device.manufacturer ?: "",
                model = device.model ?: "",
            )
        } else {
            DeviceComponentModel.Empty
        }

    fun toLibDevice(deviceComponentModel: DeviceComponentModel): Device? =
        when (deviceComponentModel) {
            DeviceComponentModel.Empty -> null
            is DeviceComponentModel.Specified -> Device(
                type = deviceComponentModel.type,
                manufacturer = deviceComponentModel.manufacturer.ifBlank { null },
                model = deviceComponentModel.model.ifBlank { null }
            )
        }
}