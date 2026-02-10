package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.MenstruationPeriod
import java.time.Instant
import java.time.ZoneOffset

class MenstruationPeriodEditor() : Editor<MenstruationPeriodRecord, MenstruationPeriod>() {

    override fun toModel(
        record: MenstruationPeriodRecord,
        mapper: MetadataMapper,
    ): MenstruationPeriod = MenstruationPeriod(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
    )

    override fun toRecord(
        validModel: MenstruationPeriod,
        mapper: MetadataMapper,
    ): MenstruationPeriodRecord = MenstruationPeriodRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
    )

    override fun createDefault(): MenstruationPeriodRecord {
        val instant = Instant.now()
        return MenstruationPeriodRecord(
            startTime = instant.minusSeconds(3600 * 24 * 3), // 3 days ago
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
        )
    }
}