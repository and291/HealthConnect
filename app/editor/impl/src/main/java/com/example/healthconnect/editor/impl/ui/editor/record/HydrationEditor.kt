package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.liters
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.Hydration
import java.time.Instant
import java.time.ZoneOffset

class HydrationEditor() : Editor<HydrationRecord, Hydration>() {

    override fun toModel(
        record: HydrationRecord,
        mapper: MetadataMapper,
    ): Hydration = Hydration(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        volume = ValueField.Dbl(
            parsedValue = record.volume.inLiters,
            type = ValueField.Type.VolumeOfWater(),
        ),
    )

    override fun toRecord(
        validModel: Hydration,
        mapper: MetadataMapper,
    ): HydrationRecord = HydrationRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        volume = (validModel.volume as ValueField.Dbl).parsedValue!!.liters,
    )

    override fun createDefault(): HydrationRecord {
        val instant = Instant.now()
        return HydrationRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            volume = 0.25.liters,
        )
    }
}