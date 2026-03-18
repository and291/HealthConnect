package com.example.healthconnect.editor.api.ui.mapper

import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField

class MetadataMapper(
    private val deviceMapper: DeviceMapper
) {

    fun toEntity(metadata: Metadata): MetadataField = MetadataField(
        recordingMethod = SelectorField(
            value = metadata.recordingMethod,
            type = SelectorField.Type.RecordingMethod(),
            priority = Int.MAX_VALUE - 100,
        ),
        id = StringField(
            value = metadata.id,
            type = StringField.Type.MetadataId(),
            readOnly = true,
            priority = Int.MAX_VALUE - 96,
        ),
        dataOriginPackageName = StringField(
            value = metadata.dataOrigin.packageName,
            type = StringField.Type.MetadataDataOrigin(),
            readOnly = true,
            priority = Int.MAX_VALUE - 95,
        ),
        lastModifiedTime = StringField(
            value = metadata.lastModifiedTime.toString(),
            type = StringField.Type.MetadataLastModifiedTime(),
            readOnly = true,
            priority = Int.MAX_VALUE - 94,
        ),
        clientRecordId = StringField(
            value = metadata.clientRecordId ?: "",
            type = StringField.Type.MetadataClientRecordId(),
            priority = Int.MAX_VALUE - 98,
        ),
        clientRecordVersion = StringField(
            value = metadata.clientRecordVersion.toString(),
            type = StringField.Type.MetadataClientRecordVersion(),
            priority = Int.MAX_VALUE - 97,
        ),
        deviceFieldComponentModel = deviceMapper.toEntity(metadata.device)
    )

    fun toLibMetadata(
        metadataEditorModel: MetadataField,
    ): Metadata = when (metadataEditorModel.recordingMethod.value) {
        Metadata.Companion.RECORDING_METHOD_UNKNOWN -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.unknownRecordingMethodWithId(
                    id = metadataEditorModel.id.value,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)
                )
            },
            withClientRecordId = {
                Metadata.Companion.unknownRecordingMethod(
                    clientRecordId = metadataEditorModel.clientRecordId.value,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.value.toLongOrNull() ?: 0L,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel),
                )
            },
            deviceUpdate = {
                Metadata.Companion.unknownRecordingMethod(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel),
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_ACTIVELY_RECORDED -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.activelyRecordedWithId(
                    metadataEditorModel.id.value,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.activelyRecorded(
                    clientRecordId = metadataEditorModel.clientRecordId.value,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.value.toLongOrNull() ?: 0L,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.activelyRecorded(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!, //TODO fix possible NPE
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_AUTOMATICALLY_RECORDED -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.autoRecordedWithId(
                    metadataEditorModel.id.value,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.autoRecorded(
                    clientRecordId = metadataEditorModel.clientRecordId.value,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.value.toLongOrNull() ?: 0L,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.autoRecorded(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!, //TODO fix possible NPE
                )
            }

        )

        Metadata.Companion.RECORDING_METHOD_MANUAL_ENTRY -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.manualEntryWithId(
                    metadataEditorModel.id.value,
                    deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.manualEntry(
                    clientRecordId = metadataEditorModel.clientRecordId.value,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.value.toLongOrNull() ?: 0L,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!, //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.manualEntry(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceFieldComponentModel)!!, //TODO fix possible NPE
                )
            }
        )

        else -> throw NotImplementedError()
    }

    private fun MetadataField.defineFactoryMethod(
        withId: (metadataEditorModel: MetadataField) -> Metadata,
        withClientRecordId: (metadataEditorModel: MetadataField) -> Metadata,
        deviceUpdate: (metadataEditorModel: MetadataField) -> Metadata,
    ): Metadata {
        if (id.value.isNotBlank()) {
            return withId(this)
        }
        return if (clientRecordId.value.isNotBlank()) {
            withClientRecordId(this)
        } else {
            deviceUpdate(this)
        }
    }
}