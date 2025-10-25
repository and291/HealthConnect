package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Temperature
import androidx.health.connect.client.units.celsius
import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BodyTemperatureModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BodyTemperatureEditor() : Editor<BodyTemperatureRecord, BodyTemperatureModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BodyTemperatureModel,
        event: ModelModificationEvent,
    ): BodyTemperatureModel = when (event) {
        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorComponentModel.Type.MeasurementLocationBodyTemperature -> model.copy(
                measurementLocation = event.selector
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueComponentModel.Type.Temperature -> model.copy(
                temperature = event.value
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BodyTemperatureRecord,
        mapper: MetadataMapper,
    ): BodyTemperatureModel = BodyTemperatureModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        temperature = DoubleValueComponentModel.Valid(
            parsedValue = record.temperature.inCelsius,
            type = DoubleValueComponentModel.Type.Temperature(),
        ),
        measurementLocation = SelectorComponentModel.Valid(
            value = record.measurementLocation, //TODO validate data from lib
            type = SelectorComponentModel.Type.MeasurementLocationBodyTemperature(),
        )
    )

    override fun toRecord(
        validModel: BodyTemperatureModel,
        mapper: MetadataMapper,
    ): BodyTemperatureRecord = BodyTemperatureRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        temperature = (validModel.temperature as DoubleValueComponentModel.Valid).parsedValue.celsius,
        measurementLocation = (validModel.measurementLocation as SelectorComponentModel.Valid).value
    )

    override fun createDefault(): BodyTemperatureRecord = BodyTemperatureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        temperature = Temperature.Companion.celsius(36.6)
    )
}