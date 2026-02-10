package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.meters
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.domain.record.Distance
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class DistanceEditor() : Editor<DistanceRecord, Distance>() {

    override fun toModel(
        record: DistanceRecord,
        mapper: MetadataMapper,
    ): Distance = Distance(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        distance = ValueField.Dbl(
            parsedValue = record.distance.inMeters,
            type = ValueField.Type.Distance(),
        ),
    )

    override fun toRecord(
        validModel: Distance,
        mapper: MetadataMapper,
    ): DistanceRecord = DistanceRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        distance = (validModel.distance as ValueField.Dbl).parsedValue!!.meters,
    )

    override fun createDefault(): DistanceRecord {
        val instant = Instant.now()
        return DistanceRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            distance = 100.0.meters,
        )
    }
}