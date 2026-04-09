package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Temperature
import androidx.health.connect.client.units.celsius
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.models.api.domain.record.BasalBodyTemperature
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class BasalBodyTemperatureEditor() : Editor<BasalBodyTemperatureRecord, BasalBodyTemperature>() {

    override fun toModel(
        record: BasalBodyTemperatureRecord,
        mapper: MetadataMapper,
    ): BasalBodyTemperature =
        BasalBodyTemperature(
            time = TimeField.Instantaneous(
                instant = record.time,
                zoneOffset = record.zoneOffset,
                priority = 0
            ),
            metadata = mapper.toEntity(record.metadata),
            temperature = ValueField.Dbl(
                parsedValue = record.temperature.inCelsius,
                type = ValueField.Type.Temperature(),
                priority = 1
            ),
            measurementLocation = SelectorField(
                value = record.measurementLocation, //TODO validate data from lib
                type = SelectorField.Type.MeasurementLocationBodyTemperature(),
                priority = 2
            )
        )

    override fun toRecord(
        validModel: BasalBodyTemperature,
        mapper: MetadataMapper,
    ): BasalBodyTemperatureRecord = BasalBodyTemperatureRecord(
        time = validModel.getValidTime().instant,
        zoneOffset = validModel.getValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        temperature = (validModel.temperature as ValueField.Dbl).parsedValue!!.celsius,
        measurementLocation = validModel.measurementLocation.value
    )

    override fun createDefault(): BasalBodyTemperatureRecord = BasalBodyTemperatureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        temperature = Temperature.celsius(36.6)
    )
}