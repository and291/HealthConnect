package com.example.healthconnect.ui.screen.component.metadata.mapper

import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.domain.entity.metadata.MetadataEntity

class MetadataMapper(
    private val deviceMapper: DeviceMapper
) {

    fun toEntity(metadata: Metadata): MetadataEntity = MetadataEntity(
        recordingMethod = metadata.recordingMethod,
        id = metadata.id,
        dataOriginPackageName = metadata.dataOrigin.packageName,
        lastModifiedTime = metadata.lastModifiedTime,
        clientRecordId = metadata.clientRecordId ?: "",
        clientRecordVersion = metadata.clientRecordVersion,
        deviceEntity = deviceMapper.toEntity(metadata.device)
    )

    fun toLibMetadata(
        metadataEntity: MetadataEntity,
    ): Metadata = when (metadataEntity.recordingMethod) {
        Metadata.Companion.RECORDING_METHOD_UNKNOWN -> metadataEntity.defineFactoryMethod(
            withId = {
                Metadata.Companion.unknownRecordingMethodWithId(
                    id = metadataEntity.id,
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)
                )
            },
            withClientRecordId = {
                Metadata.Companion.unknownRecordingMethod(
                    clientRecordId = metadataEntity.clientRecordId,
                    clientRecordVersion = metadataEntity.clientRecordVersion,
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity),
                )
            },
            deviceUpdate = {
                Metadata.Companion.unknownRecordingMethod(
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity),
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_ACTIVELY_RECORDED -> metadataEntity.defineFactoryMethod(
            withId = {
                Metadata.Companion.activelyRecordedWithId(
                    metadataEntity.id,
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.activelyRecorded(
                    clientRecordId = metadataEntity.clientRecordId,
                    clientRecordVersion = metadataEntity.clientRecordVersion,
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.activelyRecorded(
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!, //TODO fix possible NPE
                )
            }
        )

        Metadata.Companion.RECORDING_METHOD_AUTOMATICALLY_RECORDED -> metadataEntity.defineFactoryMethod(
            withId = {
                Metadata.Companion.autoRecordedWithId(
                    metadataEntity.id,
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.autoRecorded(
                    clientRecordId = metadataEntity.clientRecordId,
                    clientRecordVersion = metadataEntity.clientRecordVersion,
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!,  //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.autoRecorded(
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!, //TODO fix possible NPE
                )
            }

        )

        Metadata.Companion.RECORDING_METHOD_MANUAL_ENTRY -> metadataEntity.defineFactoryMethod(
            withId = {
                Metadata.Companion.manualEntryWithId(
                    metadataEntity.id,
                    deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!, //TODO fix possible NPE
                )
            },
            withClientRecordId = {
                Metadata.Companion.manualEntry(
                    clientRecordId = metadataEntity.clientRecordId,
                    clientRecordVersion = metadataEntity.clientRecordVersion,
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!, //TODO fix possible NPE
                )
            },
            deviceUpdate = {
                Metadata.Companion.manualEntry(
                    device = deviceMapper.toLibDevice(metadataEntity.deviceEntity)!!, //TODO fix possible NPE
                )
            }
        )

        else -> throw NotImplementedError()
    }

    /**
     * //todo(Из попытки убрать бойлерплейт получилась какая-то херня)
     * Ещё и device является обязательным параметром для некоторых типов записей.
     */
    private fun MetadataEntity.defineFactoryMethod(
        withId: (metadataEntity: MetadataEntity) -> Metadata,
        withClientRecordId: (metadataEntity: MetadataEntity) -> Metadata,
        deviceUpdate: (metadataEntity: MetadataEntity) -> Metadata,
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