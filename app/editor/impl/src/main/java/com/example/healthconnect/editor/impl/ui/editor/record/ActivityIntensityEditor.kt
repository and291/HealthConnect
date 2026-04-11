package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.ActivityIntensityRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.models.api.domain.record.ActivityIntensity
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class ActivityIntensityEditor() : Editor<ActivityIntensityRecord, ActivityIntensity>() {

    override fun toModel(
        record: ActivityIntensityRecord,
        mapper: MetadataMapper,
    ): ActivityIntensity =
        ActivityIntensity(
            time = TimeField.Interval(
                startTime = record.startTime,
                startZoneOffset = record.startZoneOffset,
                endTime = record.endTime,
                endZoneOffset = record.endZoneOffset,
                priority = 0
            ),
            metadata = mapper.toEntity(record.metadata),
            intensity = SelectorField(
                value = record.activityIntensityType,
                type = SelectorField.Type.ActivityIntensityType(),
                priority = 1
            ),
        )

    override fun toRecord(
        validModel: ActivityIntensity,
        mapper: MetadataMapper,
    ): ActivityIntensityRecord = ActivityIntensityRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        activityIntensityType = validModel.intensity.value,
    )

    override fun createDefault(): ActivityIntensityRecord {
        val instant = Instant.now()
        return ActivityIntensityRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            activityIntensityType = ActivityIntensityRecord.ACTIVITY_INTENSITY_TYPE_MODERATE,
        )
    }
}