package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.models.api.domain.record.FloorsClimbed
import java.time.Instant
import java.time.ZoneOffset

class FloorsClimbedEditor() : Editor<FloorsClimbedRecord, FloorsClimbed>() {

    override fun toModel(
        record: FloorsClimbedRecord,
        mapper: MetadataMapper,
    ): FloorsClimbed = FloorsClimbed(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        floors = ValueField.Dbl(
            parsedValue = record.floors,
            type = ValueField.Type.FloorsClimbed(),
            priority = 1
        ),
    )

    override fun toRecord(
        validModel: FloorsClimbed,
        mapper: MetadataMapper,
    ): FloorsClimbedRecord = FloorsClimbedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        floors = (validModel.floors as ValueField.Dbl).parsedValue!!,
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