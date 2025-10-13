package com.example.healthconnect.ui.screen.component.metadata.mapper

import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.domain.entity.metadata.DeviceEntity

class DeviceMapper {

    fun toEntity(device: Device?): DeviceEntity =
        if (device != null) {
            DeviceEntity.Specified(
                type = device.type,
                manufacturer = device.manufacturer ?: "",
                model = device.model ?: "",
            )
        } else {
            DeviceEntity.Empty
        }

    fun toLibDevice(deviceEntity: DeviceEntity): Device? =
        when (deviceEntity) {
            DeviceEntity.Empty -> null
            is DeviceEntity.Specified -> Device(
                type = deviceEntity.type,
                manufacturer = deviceEntity.manufacturer.ifBlank { null },
                model = deviceEntity.model.ifBlank { null }
            )
        }
}