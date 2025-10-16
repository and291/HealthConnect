package com.example.healthconnect.components.api.data.mapper

import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.domain.entity.metadata.DeviceEntity

//TODO remove from API
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