package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.TemperatureDelta
import androidx.health.connect.client.units.celsius
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SkinTemperatureDeltaField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.models.api.domain.record.SkinTemperature
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class SkinTemperatureEditor() : Editor<SkinTemperatureRecord, SkinTemperature>() {

    override fun toModel(
        record: SkinTemperatureRecord,
        mapper: MetadataMapper,
    ): SkinTemperature = SkinTemperature(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        baseline = ValueField.Dbl(
            parsedValue = record.baseline?.inCelsius ?: 0.0,
            type = ValueField.Type.Temperature("Baseline"),
            priority = 1
        ),
        measurementLocation = SelectorField(
            value = record.measurementLocation,
            type = SelectorField.Type.SkinTemperatureMeasurementLocation(),
            priority = 2
        ),
        deltas = ListField(
            items = record.deltas.map {
                SkinTemperatureDeltaField(
                    time = StringField(
                        value = it.time.toString(),
                        type = StringField.Type.SkinTemperatureDeltaTime()
                    ),
                    delta = ValueField.Dbl(
                        parsedValue = it.delta.inCelsius,
                        type = ValueField.Type.TemperatureDelta()
                    )
                )
            },
            type = ListField.Type.SkinTemperatureDeltas,
            priority = 3
        )
    )

    override fun toRecord(
        validModel: SkinTemperature,
        mapper: MetadataMapper,
    ): SkinTemperatureRecord = SkinTemperatureRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        baseline = (validModel.baseline as ValueField.Dbl).parsedValue?.celsius,
        measurementLocation = validModel.measurementLocation.value,
        deltas = validModel.deltas.items.map {
            SkinTemperatureRecord.Delta(
                time = Instant.parse(it.time.value),
                delta = TemperatureDelta.celsius((it.delta as ValueField.Dbl).parsedValue!!)
            )
        }
    )

    override fun createDefault(): SkinTemperatureRecord {
        val instant = Instant.now()
        return SkinTemperatureRecord(
            startTime = instant.minusSeconds(3600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            deltas = emptyList(),
        )
    }
}
