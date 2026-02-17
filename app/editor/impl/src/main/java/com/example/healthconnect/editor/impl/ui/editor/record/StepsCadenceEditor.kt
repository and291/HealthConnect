package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.StepsCadenceSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.editor.api.domain.record.StepsCadence
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class StepsCadenceEditor() : Editor<StepsCadenceRecord, StepsCadence>() {

    override fun toModel(
        record: StepsCadenceRecord,
        mapper: MetadataMapper,
    ): StepsCadence = StepsCadence(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        samples = ListField(
            items = record.samples.map {
                StepsCadenceSampleField(
                    time = it.time,
                    cadence = ValueField.Dbl(
                        parsedValue = it.rate,
                        type = ValueField.Type.StepsCadence(),
                    )
                )
            },
            type = ListField.Type.StepsCadenceSamples
        )
    )

    @Suppress("UNCHECKED_CAST")
    override fun toRecord(
        validModel: StepsCadence,
        mapper: MetadataMapper,
    ): StepsCadenceRecord = StepsCadenceRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        samples = validModel.samples.items.map {
            StepsCadenceRecord.Sample(
                time = it.time,
                rate = (it.cadence as ValueField.Dbl).parsedValue!!
            )
        }
    )

    override fun createDefault(): StepsCadenceRecord {
        val instant = Instant.now()
        return StepsCadenceRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            samples = emptyList()
        )
    }
}
