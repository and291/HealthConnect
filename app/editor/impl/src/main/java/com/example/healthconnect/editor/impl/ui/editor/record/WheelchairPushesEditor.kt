package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.WheelchairPushesRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.domain.record.WheelchairPushes
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class WheelchairPushesEditor() : Editor<WheelchairPushesRecord, WheelchairPushes>() {

    override fun toModel(
        record: WheelchairPushesRecord,
        mapper: MetadataMapper,
    ): WheelchairPushes = WheelchairPushes(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        count = ValueField.Lng(
            parsedValue = record.count,
            type = ValueField.Type.WheelchairPushesCount(),
        ),
    )

    override fun toRecord(
        validModel: WheelchairPushes,
        mapper: MetadataMapper,
    ): WheelchairPushesRecord = WheelchairPushesRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        count = (validModel.count as ValueField.Lng).parsedValue!!,
    )

    override fun createDefault(): WheelchairPushesRecord {
        val instant = Instant.now()
        return WheelchairPushesRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            count = 50,
        )
    }
}
