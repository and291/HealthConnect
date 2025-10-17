package com.example.healthconnect.utilty.impl.ui.mapper

import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.ui.model.DeviceModel

class DeviceMapper {

    fun toEntity(device: Device?): DeviceModel =
        if (device != null) {
            DeviceModel.Specified(
                type = device.type,
                manufacturer = device.manufacturer ?: "",
                model = device.model ?: "",
            )
        } else {
            DeviceModel.Empty
        }

    fun toLibDevice(deviceModel: DeviceModel): Device? =
        when (deviceModel) {
            DeviceModel.Empty -> null
            is DeviceModel.Specified -> Device(
                type = deviceModel.type,
                manufacturer = deviceModel.manufacturer.ifBlank { null },
                model = deviceModel.model.ifBlank { null }
            )
        }
}