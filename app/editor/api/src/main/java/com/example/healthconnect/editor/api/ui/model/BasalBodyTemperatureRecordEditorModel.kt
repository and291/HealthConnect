package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.BodyTemperatureMeasurementLocationEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel

data class BasalBodyTemperatureRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val temperatureEditorModel: TemperatureEditorModel,
    val measurementLocation: BodyTemperatureMeasurementLocationEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            temperatureEditorModel is TemperatureEditorModel.Valid &&
            measurementLocation is BodyTemperatureMeasurementLocationEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: CommonRecordModificationEvent): RecordEditorModel = when (event) {
        is CommonRecordModificationEvent.OnMeasurementLocationSelected -> copy(
            measurementLocation = event.location
        )
        is CommonRecordModificationEvent.OnMetaModelChanged -> copy(
            metadataEditorModel = event.metaModel
        )
        is CommonRecordModificationEvent.OnTemperatureChanged -> copy(
            temperatureEditorModel = event.temperatureEditorModel
        )
        is CommonRecordModificationEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )
        else -> TODO()
    }
}