package com.example.healthconnect.editor.api.ui.mapper

import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField

class DeviceMapper {

    fun toEntity(device: Device?): DeviceField =
        if (device != null) {
            DeviceField.Specified(
                type = device.type,
                manufacturer = device.manufacturer ?: "",
                model = device.model ?: "",
            )
        } else {
            DeviceField.Empty()
        }

    fun toLibDevice(deviceFieldComponentModel: DeviceField): Device? =
        when (deviceFieldComponentModel) {
            is DeviceField.Empty -> null
            is DeviceField.Specified -> Device(
                type = deviceFieldComponentModel.type,
                manufacturer = deviceFieldComponentModel.manufacturer.ifBlank { null },
                model = deviceFieldComponentModel.model.ifBlank { null }
            )
        }
}