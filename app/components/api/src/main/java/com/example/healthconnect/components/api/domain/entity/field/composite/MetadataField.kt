package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.ComponentModel
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

    override fun containsInstanceId(instanceId: UUID): Boolean = when (instanceId) {
        recordingMethod.instanceId, id.instanceId, dataOriginPackageName.instanceId,
        lastModifiedTime.instanceId, clientRecordId.instanceId, clientRecordVersion.instanceId,
        deviceFieldComponentModel.instanceId -> true
        else -> false
    }

    override fun updateFieldByInstanceId(
        instanceId: UUID,
        newField: ComponentModel,
    ): ComponentModel = when (instanceId) {
        recordingMethod.instanceId -> copy(recordingMethod = newField as SelectorField)
        id.instanceId -> copy(id = newField as StringField)
        dataOriginPackageName.instanceId -> copy(dataOriginPackageName = newField as StringField)
        lastModifiedTime.instanceId -> copy(lastModifiedTime = newField as StringField)
        clientRecordId.instanceId -> copy(clientRecordId = newField as StringField)
        clientRecordVersion.instanceId -> copy(clientRecordVersion = newField as StringField)
        deviceFieldComponentModel.instanceId -> copy(deviceFieldComponentModel = newField as DeviceField)
        else -> this
    }
}