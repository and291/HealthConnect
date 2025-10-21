package com.example.healthconnect.utilty.impl.ui.screen.record.model

import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocations
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel

data class BasalBodyTemperatureEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val temperatureEditorModel: TemperatureEditorModel,
    @property:BodyTemperatureMeasurementLocations
    val measurementLocation: Int = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
) : EditorModel() {

    fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            temperatureEditorModel is TemperatureEditorModel.Valid &&
            metadataEditorModel.isValid() //TODO do i need to validate measurementLocation value?
}