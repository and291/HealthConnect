package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import java.util.UUID

data class MetadataField(
    val recordingMethod: SelectorField,
    val id: StringField,
    val dataOriginPackageName: StringField,
    val lastModifiedTime: StringField,
    val clientRecordId: StringField,
    val clientRecordVersion: StringField,
    val deviceFieldComponentModel: DeviceField = DeviceField.Empty(),
    override val instanceId: UUID = UUID.randomUUID(),
) : Composite(instanceId) {

    override fun isValid(): Boolean {
        return recordingMethod.isValid() &&
                id.isValid() &&
                dataOriginPackageName.isValid() &&
                lastModifiedTime.isValid() &&
                clientRecordId.isValid() &&
                clientRecordVersion.isValid() &&
                deviceFieldComponentModel.isValid() &&
                isClientRecordVersionValid()
    }

    private fun isClientRecordVersionValid(): Boolean {
        if (clientRecordVersion.value.isBlank()) return true
        return clientRecordVersion.value.toLongOrNull() != null
    }
}