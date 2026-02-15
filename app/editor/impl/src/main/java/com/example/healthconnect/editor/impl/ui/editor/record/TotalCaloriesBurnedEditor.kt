package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.kilocalories
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.domain.record.TotalCaloriesBurned
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class TotalCaloriesBurnedEditor() : Editor<TotalCaloriesBurnedRecord, TotalCaloriesBurned>() {

    override fun toModel(
        record: TotalCaloriesBurnedRecord,
        mapper: MetadataMapper,
    ): TotalCaloriesBurned = TotalCaloriesBurned(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        energy = ValueField.Dbl(
            parsedValue = record.energy.inKilocalories,
            type = ValueField.Type.Energy(),
        ),
    )

    override fun toRecord(
        validModel: TotalCaloriesBurned,
        mapper: MetadataMapper,
    ): TotalCaloriesBurnedRecord = TotalCaloriesBurnedRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        energy = (validModel.energy as ValueField.Dbl).parsedValue!!.kilocalories,
    )

    override fun createDefault(): TotalCaloriesBurnedRecord {
        val instant = Instant.now()
        return TotalCaloriesBurnedRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            energy = 100.0.kilocalories,
        )
    }
}
