package com.example.healthconnect.utilty.impl.ui.screen.record.model

import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocations
import com.example.healthconnect.components.api.ui.model.MetadataModel
import com.example.healthconnect.components.api.ui.model.InstantModel
import com.example.healthconnect.components.api.ui.model.TemperatureModel

data class BasalBodyTemperatureModel(
    val instantModel: InstantModel,
    val metadataModel: MetadataModel,
    val temperatureModel: TemperatureModel,
    @property:BodyTemperatureMeasurementLocations
    val measurementLocation: Int = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
)