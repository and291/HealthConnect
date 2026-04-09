package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.CyclingPedalingCadenceSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.models.api.domain.record.CyclingPedalingCadence
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class CyclingPedalingCadenceEditor() : Editor<CyclingPedalingCadenceRecord, CyclingPedalingCadence>() {

    override fun toModel(
        record: CyclingPedalingCadenceRecord,
        mapper: MetadataMapper,
    ): CyclingPedalingCadence = CyclingPedalingCadence(
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
                CyclingPedalingCadenceSampleField(
                    time = it.time,
                    cadence = ValueField.Dbl(
                        parsedValue = it.revolutionsPerMinute,
                        type = ValueField.Type.CyclingPedalingCadence(),
                    )
                )
            },
            type = ListField.Type.CyclingPedalingCadenceSamples,
            priority = 1
        )
    )

    override fun toRecord(
        validModel: CyclingPedalingCadence,
        mapper: MetadataMapper,
    ): CyclingPedalingCadenceRecord = CyclingPedalingCadenceRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        samples = validModel.samples.items.map {
            CyclingPedalingCadenceRecord.Sample(
                time = it.time,
                revolutionsPerMinute = (it.cadence as ValueField.Dbl).parsedValue!!
            )
        }
    )

    override fun createDefault(): CyclingPedalingCadenceRecord {
        val instant = Instant.now()
        return CyclingPedalingCadenceRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            samples = emptyList()
        )
    }
}
