package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.FloorsClimbedModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class FloorsClimbedEditor() : Editor<FloorsClimbedRecord, FloorsClimbedModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: FloorsClimbedModel,
        event: ModelModificationEvent,
    ): FloorsClimbedModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.FloorsClimbed -> model.copy(
                floors = event.value
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: FloorsClimbedRecord,
        mapper: MetadataMapper,
    ): FloorsClimbedModel = FloorsClimbedModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        floors = ValueComponentModel.Dbl(
            parsedValue = record.floors,
            type = ValueComponentModel.Type.FloorsClimbed(),
        ),
    )

    override fun toRecord(
        validModel: FloorsClimbedModel,
        mapper: MetadataMapper,
    ): FloorsClimbedRecord = FloorsClimbedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        floors = (validModel.floors as ValueComponentModel.Dbl).parsedValue!!,
    )

    override fun createDefault(): FloorsClimbedRecord {
        val instant = Instant.now()
        return FloorsClimbedRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            floors = 1.0,
        )
    }
}