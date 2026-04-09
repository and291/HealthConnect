package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.calories
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.models.api.domain.record.ActiveCaloriesBurned
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class ActiveCaloriesBurnedEditor() : Editor<ActiveCaloriesBurnedRecord, ActiveCaloriesBurned>() {

    override fun toModel(
        record: ActiveCaloriesBurnedRecord,
        mapper: MetadataMapper,
    ): ActiveCaloriesBurned =
        ActiveCaloriesBurned(
            time = TimeField.Interval(
                startTime = record.startTime,
                startZoneOffset = record.startZoneOffset,
                endTime = record.endTime,
                endZoneOffset = record.endZoneOffset,
                priority = 0
            ),
            metadata = mapper.toEntity(record.metadata),
            energy = ValueField.Dbl(
                parsedValue = record.energy.inCalories,
                type = ValueField.Type.Energy(),
                priority = 1
            ),
        )

    override fun toRecord(
        validModel: ActiveCaloriesBurned,
        mapper: MetadataMapper,
    ): ActiveCaloriesBurnedRecord = ActiveCaloriesBurnedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        energy = (validModel.energy as ValueField.Dbl).parsedValue!!.calories,
    )

    override fun createDefault(): ActiveCaloriesBurnedRecord {
        val instant = Instant.now()
        return ActiveCaloriesBurnedRecord(
            startTime = instant.minusSeconds(60),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            energy = 60.calories,
        )
    }
}