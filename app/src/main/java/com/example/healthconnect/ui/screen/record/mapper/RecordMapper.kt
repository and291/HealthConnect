package com.example.healthconnect.ui.screen.record.mapper

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.Record
import com.example.healthconnect.ui.screen.component.metadata.mapper.MetadataMapper
import com.example.healthconnect.ui.screen.record.model.BasalBodyTemperatureModel

class RecordMapper(
    private val metadataMapper: MetadataMapper
) {

    fun toUiModel(record: Record) = when(record) {
        is BasalBodyTemperatureRecord -> BasalBodyTemperatureModel(
            time = record.time,
            zoneOffset = record.zoneOffset,
            metadataEntity = metadataMapper.toEntity(record.metadata),
            temperature = record.temperature,
            measurementLocation = record.measurementLocation
        )
        else -> TODO()
    }
}