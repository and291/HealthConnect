package com.example.healthconnect.ui.screen.record.metadata

import androidx.health.connect.client.records.metadata.Device
import androidx.health.connect.client.records.metadata.Metadata

class MetadataMapper {

    fun toUiModel(metadata: Metadata): MetadataEditorViewModel.MetadataModel =
        MetadataEditorViewModel.MetadataModel(
            recordingMethod = metadata.recordingMethod,
            id = metadata.id,
            dataOriginPackageName = metadata.dataOrigin.packageName,
            lastModifiedTime = metadata.lastModifiedTime,
            clientRecordId = metadata.clientRecordId ?: "",
            clientRecordVersion = metadata.clientRecordVersion,
        )

    fun toMetadata(
        metadataModel: MetadataEditorViewModel.MetadataModel,
        device: Device?,
    ): Metadata {
        return when (metadataModel.recordingMethod) {
            Metadata.RECORDING_METHOD_UNKNOWN -> metadataModel.defineFactoryMethod(
                withId = {
                    Metadata.unknownRecordingMethodWithId(
                        id = metadataModel.id,
                        device = device
                    )
                },
                withClientRecordId = {
                    Metadata.unknownRecordingMethod(
                        clientRecordId = metadataModel.clientRecordId,
                        clientRecordVersion = metadataModel.clientRecordVersion,
                        device = device,
                    )
                },
                deviceUpdate = {
                    Metadata.unknownRecordingMethod(
                        device = device
                    )
                }
            )

            Metadata.RECORDING_METHOD_ACTIVELY_RECORDED -> metadataModel.defineFactoryMethod(
                withId = {
                    Metadata.activelyRecordedWithId(
                        metadataModel.id,
                        device!!, //TODO fix possible NPE
                    )
                },
                withClientRecordId = {
                    Metadata.activelyRecorded(
                        clientRecordId = metadataModel.clientRecordId,
                        clientRecordVersion = metadataModel.clientRecordVersion,
                        device = device!!,  //TODO fix possible NPE
                    )
                },
                deviceUpdate = {
                    Metadata.activelyRecorded(
                        device = device!!, //TODO fix possible NPE
                    )
                }
            )

            Metadata.RECORDING_METHOD_AUTOMATICALLY_RECORDED -> metadataModel.defineFactoryMethod(
                withId = {
                    Metadata.autoRecordedWithId(
                        metadataModel.id,
                        device!!, //TODO fix possible NPE
                    )
                },
                withClientRecordId = {
                    Metadata.autoRecorded(
                        clientRecordId = metadataModel.clientRecordId,
                        clientRecordVersion = metadataModel.clientRecordVersion,
                        device = device!!,  //TODO fix possible NPE
                    )
                },
                deviceUpdate = {
                    Metadata.autoRecorded(
                        device = device!!, //TODO fix possible NPE
                    )
                }

            )

            Metadata.RECORDING_METHOD_MANUAL_ENTRY -> metadataModel.defineFactoryMethod(
                withId = {
                    Metadata.manualEntryWithId(
                        metadataModel.id,
                        device!!, //TODO fix possible NPE
                    )
                },
                withClientRecordId = {
                    Metadata.manualEntry(
                        clientRecordId = metadataModel.clientRecordId,
                        clientRecordVersion = metadataModel.clientRecordVersion,
                        device = device!!, //TODO fix possible NPE
                    )
                },
                deviceUpdate = {
                    Metadata.manualEntry(
                        device = device!!, //TODO fix possible NPE
                    )
                }
            )

            else -> throw NotImplementedError()
        }
    }

    /**
     * //todo(Из попытки убрать бойлерплейт получилась какая-то херня)
     * Ещё и device является обязательным параметром для некоторых типов записей.
     */
    private fun MetadataEditorViewModel.MetadataModel.defineFactoryMethod(
        withId: (metadataModel: MetadataEditorViewModel.MetadataModel) -> Metadata,
        withClientRecordId: (metadataModel: MetadataEditorViewModel.MetadataModel) -> Metadata,
        deviceUpdate: (metadataModel: MetadataEditorViewModel.MetadataModel) -> Metadata,
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