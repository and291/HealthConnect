package com.example.healthconnect.ui.screen.component.metadata.mapper

import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.ui.screen.component.metadata.model.MetadataModel

class MetadataMapper(
    private val deviceMapper: DeviceMapper
) {

    fun toUiModel(metadata: Metadata): MetadataModel = MetadataModel(
        recordingMethod = metadata.recordingMethod,
        id = metadata.id,
        dataOriginPackageName = metadata.dataOrigin.packageName,
        lastModifiedTime = metadata.lastModifiedTime,
        clientRecordId = metadata.clientRecordId ?: "",
        clientRecordVersion = metadata.clientRecordVersion,
        deviceModel = deviceMapper.toUiModel(metadata.device)
    )

    fun toMetadata(
        metadataModel: MetadataModel,
    ): Metadata = when (metadataModel.recordingMethod) {
        Metadata.Companion.RECORDING_METHOD_UNKNOWN -> metadataModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.unknownRecordingMethodWithId(
                    id = metadataModel.id,
                    device = deviceMapper.toDevice(metadataModel.deviceModel)
                )
            },
            withClientRecordId = {
                Metadata.Companion.unknownRecordingMethod(
                    clientRecordId = metadataModel.clientRecordId,
                    clientRecordVersion = metadataModel.clientRecordVersion,
                    device = deviceMapper.toDevice(metadataModel.deviceModel),
                )
            },
            deviceUpdate = {
                Metadata.Companion.unknownRecordingMethod(
                    device = deviceMapper.toDevice(metadataModel.deviceModel),
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_ACTIVELY_RECORDED -> metadataModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.activelyRecordedWithId(
                    metadataModel.id,
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.activelyRecorded(
                    clientRecordId = metadataModel.clientRecordId,
                    clientRecordVersion = metadataModel.clientRecordVersion,
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.activelyRecorded(
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!, //TODO fix possible NPE
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_AUTOMATICALLY_RECORDED -> metadataModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.autoRecordedWithId(
                    metadataModel.id,
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.autoRecorded(
                    clientRecordId = metadataModel.clientRecordId,
                    clientRecordVersion = metadataModel.clientRecordVersion,
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.autoRecorded(
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!, //TODO fix possible NPE
                )
            }

        )

        Metadata.Companion.RECORDING_METHOD_MANUAL_ENTRY -> metadataModel.defineFactoryMethod(
            withId = {
                Metadata.Companion.manualEntryWithId(
                    metadataModel.id,
                    deviceMapper.toDevice(metadataModel.deviceModel)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.manualEntry(
                    clientRecordId = metadataModel.clientRecordId,
                    clientRecordVersion = metadataModel.clientRecordVersion,
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!, //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.manualEntry(
                    device = deviceMapper.toDevice(metadataModel.deviceModel)!!, //TODO fix possible NPE
                )
            }
        )

        else -> throw NotImplementedError()
    }

    /**
     * //todo(Из попытки убрать бойлерплейт получилась какая-то херня)
     * Ещё и device является обязательным параметром для некоторых типов записей.
     */
    private fun MetadataModel.defineFactoryMethod(
        withId: (metadataModel: MetadataModel) -> Metadata,
        withClientRecordId: (metadataModel: MetadataModel) -> Metadata,
        deviceUpdate: (metadataModel: MetadataModel) -> Metadata,
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