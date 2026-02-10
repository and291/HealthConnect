package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.record.RestingHeartRate
import java.time.Instant
import java.time.ZoneOffset

class RestingHeartRateEditor() : Editor<RestingHeartRateRecord, RestingHeartRate>() {

    override fun toModel(
        record: RestingHeartRateRecord,
        mapper: MetadataMapper,
    ): RestingHeartRate = RestingHeartRate(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        beatsPerMinute = ValueField.Lng(
            parsedValue = record.beatsPerMinute,
            type = ValueField.Type.BeatsPerMinute(),
        ),
    )

    override fun toRecord(
        validModel: RestingHeartRate,
        mapper: MetadataMapper,
    ): RestingHeartRateRecord = RestingHeartRateRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        beatsPerMinute = (validModel.beatsPerMinute as ValueField.Lng).parsedValue!!,
    )

    override fun createDefault(): RestingHeartRateRecord = RestingHeartRateRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        beatsPerMinute = 80,
    )
}