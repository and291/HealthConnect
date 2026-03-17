package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.HeartRateVariabilityRmssd
import java.time.Instant
import java.time.ZoneOffset

class HeartRateVariabilityRmssdEditor() :
    Editor<HeartRateVariabilityRmssdRecord, HeartRateVariabilityRmssd>() {

    override fun toModel(
        record: HeartRateVariabilityRmssdRecord,
        mapper: MetadataMapper,
    ): HeartRateVariabilityRmssd = HeartRateVariabilityRmssd(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        heartRateVariabilityMillis = ValueField.Dbl(
            parsedValue = record.heartRateVariabilityMillis,
            type = ValueField.Type.HeartRateVariabilityRmssd(),
            priority = 1
        ),
    )

    override fun toRecord(
        validModel: HeartRateVariabilityRmssd,
        mapper: MetadataMapper,
    ): HeartRateVariabilityRmssdRecord = HeartRateVariabilityRmssdRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        heartRateVariabilityMillis = (validModel.heartRateVariabilityMillis as ValueField.Dbl).parsedValue!!,
    )

    override fun createDefault(): HeartRateVariabilityRmssdRecord = HeartRateVariabilityRmssdRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        heartRateVariabilityMillis = 100.0,
    )
}