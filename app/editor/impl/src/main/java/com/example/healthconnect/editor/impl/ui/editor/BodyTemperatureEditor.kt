package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Temperature
import androidx.health.connect.client.units.celsius
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.BodyTemperatureRecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import java.time.Instant
import java.time.ZoneOffset

class BodyTemperatureEditor() : Editor<BodyTemperatureRecord, BodyTemperatureRecordEditorModel>() {

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(
        model: BodyTemperatureRecordEditorModel,
        event: RecordModificationEvent,
    ): BodyTemperatureRecordEditorModel = when (event) {
        is RecordModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorEditorModel.Type.MeasurementLocationBodyTemperature -> model.copy(
                measurementLocation = event.selector
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnMetadataChanged -> model.copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.Temperature -> model.copy(
                temperature = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnTimeChanged -> model.copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

    override fun toModel(
        record: BodyTemperatureRecord,
        metadataMapper: MetadataMapper,
    ): BodyTemperatureRecordEditorModel = BodyTemperatureRecordEditorModel(
        time = TimeEditorModel.Valid(
            instant = record.time,
            zoneOffset = record.zoneOffset
        ),
        metadata = metadataMapper.toEntity(record.metadata),
        temperature = DoubleValueEditorModel.Valid(
            parsedValue = record.temperature.inCelsius,
            type = DoubleValueEditorModel.Type.Temperature(),
        ),
        measurementLocation = SelectorEditorModel.Valid(
            value = record.measurementLocation, //TODO validate data from lib
            type = SelectorEditorModel.Type.MeasurementLocationBodyTemperature(),
        )
    )

    override fun toRecord(
        validUiModel: BodyTemperatureRecordEditorModel,
        metadataMapper: MetadataMapper,
    ): BodyTemperatureRecord = BodyTemperatureRecord(
        time = (validUiModel.time as TimeEditorModel.Valid).instant,
        zoneOffset = (validUiModel.time as TimeEditorModel.Valid).zoneOffset,
        metadata = metadataMapper.toLibMetadata(validUiModel.metadata),
        temperature = (validUiModel.temperature as DoubleValueEditorModel.Valid).parsedValue.celsius,
        measurementLocation = (validUiModel.measurementLocation as SelectorEditorModel.Valid).value
    )

    override fun createDefault(): BodyTemperatureRecord = BodyTemperatureRecord(
        time = Instant.now(),
        zoneOffset = ZoneOffset.UTC,
        metadata = Metadata.Companion.unknownRecordingMethod(),
        temperature = Temperature.Companion.celsius(36.6)
    )
}