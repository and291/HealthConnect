package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Velocity
import com.example.healthconnect.components.api.domain.entity.field.atomic.SpeedSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.models.api.domain.record.Speed
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class SpeedEditor() : Editor<SpeedRecord, Speed>() {

    override fun toModel(
        record: SpeedRecord,
        mapper: MetadataMapper,
    ): Speed = Speed(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        samples = ListField(
            items = record.samples.map {
                SpeedSampleField(
                    time = it.time,
                    speed = ValueField.Dbl(
                        parsedValue = it.speed.inMetersPerSecond,
                        type = ValueField.Type.Speed(),
                    )
                )
            },
            type = ListField.Type.SpeedSamples,
            priority = 1
        )
    )

    @Suppress("UNCHECKED_CAST")
    override fun toRecord(
        validModel: Speed,
        mapper: MetadataMapper,
    ): SpeedRecord = SpeedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        samples = validModel.samples.items.map {
            SpeedRecord.Sample(
                time = it.time,
                speed = Velocity.metersPerSecond((it.speed as ValueField.Dbl).parsedValue!!)
            )
        }
    )

    override fun createDefault(): SpeedRecord {
        val instant = Instant.now()
        return SpeedRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            samples = emptyList()
        )
    }
}
