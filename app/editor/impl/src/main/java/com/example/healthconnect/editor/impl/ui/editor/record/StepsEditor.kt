package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class StepsEditor() : Editor<StepsRecord, Steps>() {

    override fun toModel(
        record: StepsRecord,
        mapper: MetadataMapper,
    ): Steps = Steps(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        count = ValueField.Lng(
            parsedValue = record.count,
            type = ValueField.Type.StepsCount(),
            priority = 1
        ),
    )

    override fun toRecord(
        validModel: Steps,
        mapper: MetadataMapper,
    ): StepsRecord = StepsRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        count = (validModel.count as ValueField.Lng).parsedValue!!,
    )

    override fun createDefault(): StepsRecord {
        val instant = Instant.now()
        return StepsRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            count = 1000,
        )
    }
}
