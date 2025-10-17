package com.example.healthconnect.utilty.impl.ui.screen.record.mapper

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.Record
import com.example.healthconnect.components.api.ui.model.InstantModel
import com.example.healthconnect.components.api.ui.model.TemperatureModel
import com.example.healthconnect.utilty.impl.ui.mapper.MetadataMapper
import com.example.healthconnect.utilty.impl.ui.screen.record.model.BasalBodyTemperatureModel

class RecordMapper(
    private val metadataMapper: MetadataMapper
) {

    fun toUiModel(record: Record) = when(record) {
        is BasalBodyTemperatureRecord -> BasalBodyTemperatureModel(
            instantModel = InstantModel.Valid(
                instant = record.time,
                zoneOffset = record.zoneOffset
            ),
            metadataModel = metadataMapper.toEntity(record.metadata),
            temperatureModel = TemperatureModel.Valid(record.temperature.inCelsius),
            measurementLocation = record.measurementLocation
        )
        else -> TODO()
    }
}