package com.example.healthconnect.editor.api.ui.mapper

import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel

class MetadataMapper(
    private val deviceMapper: DeviceMapper
) {

    fun toEntity(metadata: Metadata): MetadataEditorModel = MetadataEditorModel(
        recordingMethod = metadata.recordingMethod,
        id = metadata.id,
        dataOriginPackageName = metadata.dataOrigin.packageName,
        lastModifiedTime = metadata.lastModifiedTime,
        clientRecordId = metadata.clientRecordId ?: "",
        clientRecordVersion = metadata.clientRecordVersion.toString(),
        deviceEditorModel = deviceMapper.toEntity(metadata.device)
    )

    fun toLibMetadata(
        metadataEditorModel: MetadataEditorModel,
    ): Metadata = when (metadataEditorModel.recordingMethod) {
        Metadata.Companion.RECORDING_METHOD_UNKNOWN -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.unknownRecordingMethodWithId(
                    id = metadataEditorModel.id,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)
                )
            },
            withClientRecordId = {
                Metadata.Companion.unknownRecordingMethod(
                    clientRecordId = metadataEditorModel.clientRecordId,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.toLong(),
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel),
                )
            },
            deviceUpdate = {
                Metadata.Companion.unknownRecordingMethod(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel),
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_ACTIVELY_RECORDED -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.activelyRecordedWithId(
                    metadataEditorModel.id,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.activelyRecorded(
                    clientRecordId = metadataEditorModel.clientRecordId,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.toLong(),
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.activelyRecorded(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!, //TODO fix possible NPE
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_AUTOMATICALLY_RECORDED -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.autoRecordedWithId(
                    metadataEditorModel.id,
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.autoRecorded(
                    clientRecordId = metadataEditorModel.clientRecordId,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.toLong(),
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.autoRecorded(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!, //TODO fix possible NPE
                )
            }

        )

        Metadata.Companion.RECORDING_METHOD_MANUAL_ENTRY -> metadataEditorModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.manualEntryWithId(
                    metadataEditorModel.id,
                    deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.manualEntry(
                    clientRecordId = metadataEditorModel.clientRecordId,
                    clientRecordVersion = metadataEditorModel.clientRecordVersion.toLong(),
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!, //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.manualEntry(
                    device = deviceMapper.toLibDevice(metadataEditorModel.deviceEditorModel)!!, //TODO fix possible NPE
                )
            }
        )

        else -> throw NotImplementedError()
    }

    /**
     * //todo(Из попытки убрать бойлерплейт получилась какая-то херня)
     * Ещё и device является обязательным параметром для некоторых типов записей.
     */
    private fun MetadataEditorModel.defineFactoryMethod(
        withId: (metadataEditorModel: MetadataEditorModel) -> Metadata,
        withClientRecordId: (metadataEditorModel: MetadataEditorModel) -> Metadata,
        deviceUpdate: (metadataEditorModel: MetadataEditorModel) -> Metadata,
    ): Metadata {
        if (id.isNotBlank()) {
            return withId(this)
        }
        return if (clientRecordId.isNotBlank()) {
            withClientRecordId(this)
        } else {
            deviceUpdate(this)
        }
    }
}