package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.HeartRateVariabilityRmssdModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class HeartRateVariabilityRmssdEditor() : Editor<HeartRateVariabilityRmssdRecord, HeartRateVariabilityRmssdModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: HeartRateVariabilityRmssdModel,
        event: ModelModificationEvent,
    ): HeartRateVariabilityRmssdModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.HeartRateVariabilityRmssd -> model.copy(
                heartRateVariabilityMillis = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: HeartRateVariabilityRmssdRecord,
        mapper: MetadataMapper,
    ): HeartRateVariabilityRmssdModel = HeartRateVariabilityRmssdModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        heartRateVariabilityMillis = ValueComponentModel.ValidDouble(
            parsedValue = record.heartRateVariabilityMillis,
            type = ValueComponentModel.Type.HeartRateVariabilityRmssd(),
        ),
    )

    override fun toRecord(
        validModel: HeartRateVariabilityRmssdModel,
        mapper: MetadataMapper,
    ): HeartRateVariabilityRmssdRecord = HeartRateVariabilityRmssdRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        heartRateVariabilityMillis = (validModel.heartRateVariabilityMillis as ValueComponentModel.ValidDouble).parsedValue,
    )

    override fun createDefault(): HeartRateVariabilityRmssdRecord = HeartRateVariabilityRmssdRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        heartRateVariabilityMillis = 100.0,
    )
}