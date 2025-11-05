package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Temperature
import androidx.health.connect.client.units.celsius
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BasalBodyTemperatureModel
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BasalBodyTemperatureEditor() : Editor<BasalBodyTemperatureRecord, BasalBodyTemperatureModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BasalBodyTemperatureModel,
        event: ModelModificationEvent,
    ): BasalBodyTemperatureModel = when (event) {
        is ModelModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorComponentModel.Type.MeasurementLocationBodyTemperature -> model.copy(
                measurementLocation = event.selector
            )

            else -> throw NotImplementedError()
        }

        is ModelModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is ModelModificationEvent.OnValueChanged -> when (event.value.type) {
            is ValueComponentModel.Type.Temperature -> model.copy(
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
        record: BasalBodyTemperatureRecord,
        mapper: MetadataMapper,
    ): BasalBodyTemperatureModel = BasalBodyTemperatureModel(
        time = TimeComponentModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        temperature = ValueComponentModel.ValidDouble(
            parsedValue = record.temperature.inCelsius,
            type = ValueComponentModel.Type.Temperature(),
        ),
        measurementLocation = SelectorComponentModel.Valid(
            value = record.measurementLocation, //TODO validate data from lib
            type = SelectorComponentModel.Type.MeasurementLocationBodyTemperature(),
        )
    )

    override fun toRecord(
        validModel: BasalBodyTemperatureModel,
        mapper: MetadataMapper,
    ): BasalBodyTemperatureRecord = BasalBodyTemperatureRecord(
        time = (validModel.time as TimeComponentModel.Valid).instant,
        zoneOffset = (validModel.time as TimeComponentModel.Valid).zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        temperature = (validModel.temperature as ValueComponentModel.ValidDouble).parsedValue.celsius,
        measurementLocation = (validModel.measurementLocation as SelectorComponentModel.Valid).value
    )

    override fun createDefault(): BasalBodyTemperatureRecord = BasalBodyTemperatureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        temperature = Temperature.Companion.celsius(36.6)
    )
}