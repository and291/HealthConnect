package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.models.api.domain.record.ElevationGained
import java.time.Instant
import java.time.ZoneOffset

class ElevationGainedEditor() : Editor<ElevationGainedRecord, ElevationGained>() {

    override fun toModel(
        record: ElevationGainedRecord,
        mapper: MetadataMapper,
    ): ElevationGained = ElevationGained(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        elevation = ValueField.Dbl(
            parsedValue = record.elevation.inMeters,
            type = ValueField.Type.Elevation(),
            priority = 1
        ),
    )

    override fun toRecord(
        validModel: ElevationGained,
        mapper: MetadataMapper,
    ): ElevationGainedRecord = ElevationGainedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        elevation = (validModel.elevation as ValueField.Dbl).parsedValue!!.meters,
    )

    override fun createDefault(): ElevationGainedRecord {
        val instant = Instant.now()
        return ElevationGainedRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            elevation = 10.0.meters,
        )
    }
}