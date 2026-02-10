package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Temperature
import androidx.health.connect.client.units.celsius
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.domain.record.BodyTemperature
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class BodyTemperatureEditor() : Editor<BodyTemperatureRecord, BodyTemperature>() {

    override fun toModel(
        record: BodyTemperatureRecord,
        mapper: MetadataMapper,
    ): BodyTemperature = BodyTemperature(
        time = TimeField.Instantaneous(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        temperature = ValueField.Dbl(
            parsedValue = record.temperature.inCelsius,
            type = ValueField.Type.Temperature(),
        ),
        measurementLocation = SelectorField(
            value = record.measurementLocation, //TODO validate data from lib
            type = SelectorField.Type.MeasurementLocationBodyTemperature(),
        )
    )

    override fun toRecord(
        validModel: BodyTemperature,
        mapper: MetadataMapper,
    ): BodyTemperatureRecord = BodyTemperatureRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        temperature = (validModel.temperature as ValueField.Dbl).parsedValue!!.celsius,
        measurementLocation = validModel.measurementLocation.value
    )

    override fun createDefault(): BodyTemperatureRecord = BodyTemperatureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        temperature = Temperature.celsius(36.6)
    )
}