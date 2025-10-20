package com.example.healthconnect.utilty.impl.ui.screen.record.mapper

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.units.celsius
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.utilty.impl.ui.mapper.MetadataMapper
import com.example.healthconnect.utilty.impl.ui.screen.record.model.BasalBodyTemperatureEditorModel
import com.example.healthconnect.utilty.impl.ui.screen.record.model.EditorModel

class RecordMapper(
    private val metadataMapper: MetadataMapper
) {

    fun toUiModel(record: Record): EditorModel = when(record) {
        is BasalBodyTemperatureRecord -> BasalBodyTemperatureEditorModel(
            timeEditorModel = TimeEditorModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadataEditorModel = metadataMapper.toEntity(record.metadata),
            temperatureEditorModel = TemperatureEditorModel.Valid(record.temperature.inCelsius),
            measurementLocation = record.measurementLocation
        )
        else -> TODO()
    }

    /**
     * Creates new Library Entity based on VALID EditorModel
     * @throws error if parameter is invalid
     * @throws error if Library Entity's validation rules (checked at instance creation time) are not satisfied
     */
    @Throws(Exception::class)
    fun toEntity(validUiModel: EditorModel): Record = when(validUiModel) {
        is BasalBodyTemperatureEditorModel -> BasalBodyTemperatureRecord(
            time = (validUiModel.timeEditorModel as TimeEditorModel.Valid).instant,
            zoneOffset = validUiModel.timeEditorModel.zoneOffset,
            metadata = metadataMapper.toLibMetadata(validUiModel.metadataEditorModel),
            temperature = (validUiModel.temperatureEditorModel as TemperatureEditorModel.Valid).temperatureCelsius.celsius,
            measurementLocation = validUiModel.measurementLocation
        )
        else -> TODO()
    }
}