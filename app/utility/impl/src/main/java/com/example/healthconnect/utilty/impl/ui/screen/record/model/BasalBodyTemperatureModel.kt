package com.example.healthconnect.utilty.impl.ui.screen.record.model

import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocations
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel

data class BasalBodyTemperatureModel(
    val timeEditorModel: TimeEditorModel,
    val metadataEditorModel: MetadataEditorModel,
    val temperatureEditorModel: TemperatureEditorModel,
    @property:BodyTemperatureMeasurementLocations
    val measurementLocation: Int = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
)