package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.RestingHeartRateModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class RestingHeartRateEditor() : Editor<RestingHeartRateRecord, RestingHeartRateModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: RestingHeartRateModel,
        event: ModelModificationEvent,
    ): RestingHeartRateModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.BeatsPerMinute -> model.copy(
                beatsPerMinute = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: RestingHeartRateRecord,
        mapper: MetadataMapper,
    ): RestingHeartRateModel = RestingHeartRateModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        beatsPerMinute = ValueComponentModel.ValidLong(
            parsedValue = record.beatsPerMinute,
            type = ValueComponentModel.Type.BeatsPerMinute(),
        ),
    )

    override fun toRecord(
        validModel: RestingHeartRateModel,
        mapper: MetadataMapper,
    ): RestingHeartRateRecord = RestingHeartRateRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        beatsPerMinute = (validModel.beatsPerMinute as ValueComponentModel.ValidLong).parsedValue,
    )

    override fun createDefault(): RestingHeartRateRecord = RestingHeartRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        beatsPerMinute = 80,
    )
}