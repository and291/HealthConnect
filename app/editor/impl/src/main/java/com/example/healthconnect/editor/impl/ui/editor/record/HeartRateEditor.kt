package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.HeartRateSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.editor.api.domain.record.HeartRate
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class HeartRateEditor() : Editor<HeartRateRecord, HeartRate>() {

    override fun toModel(
        record: HeartRateRecord,
        mapper: MetadataMapper,
    ): HeartRate = HeartRate(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        samples = ListField(
            items = record.samples.map {
                HeartRateSampleField(
                    time = it.time,
                    heartRate = ValueField.Lng(
                        parsedValue = it.beatsPerMinute,
                        type = ValueField.Type.BeatsPerMinute(),
                    )
                )
            },
            type = ListField.Type.HeartRateSamples
        )
    )

    @Suppress("UNCHECKED_CAST")
    override fun toRecord(
        validModel: HeartRate,
        mapper: MetadataMapper,
    ): HeartRateRecord = HeartRateRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        samples = validModel.samples.items.map {
            HeartRateRecord.Sample(
                time = it.time,
                beatsPerMinute = (it.heartRate as ValueField.Lng).parsedValue!!
            )
        }
    )

    override fun createDefault(): HeartRateRecord {
        val instant = Instant.now()
        return HeartRateRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            samples = emptyList()
        )
    }
}
