package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.ActivityIntensityRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.ActivityIntensityModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class ActivityIntensityEditor() : Editor<ActivityIntensityRecord, ActivityIntensityModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: ActivityIntensityModel,
        event: ModelModificationEvent,
    ): ActivityIntensityModel = when (event) {
        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorComponentModel.Type.ActivityIntensityType -> model.copy(
                intensity = event.selector
            )

            else -> throw NotImplementedError()
        }

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: ActivityIntensityRecord,
        mapper: MetadataMapper,
    ): ActivityIntensityModel = ActivityIntensityModel(
        time = TimeComponentModel.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        intensity = SelectorComponentModel(
            value = record.activityIntensityType,
            type = SelectorComponentModel.Type.ActivityIntensityType(),
        ),
    )

    override fun toRecord(
        validModel: ActivityIntensityModel,
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