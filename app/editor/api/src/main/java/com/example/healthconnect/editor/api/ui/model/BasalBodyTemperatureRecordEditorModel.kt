package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel

data class BasalBodyTemperatureRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val temperatureEditorModel: TemperatureEditorModel,
    val measurementLocation: SelectorEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            temperatureEditorModel is TemperatureEditorModel.Valid &&
            measurementLocation is SelectorEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordEditEvent): RecordEditorModel = when (event) {
        is RecordEditEvent.OnMeasurementLocationSelected -> copy(
            measurementLocation = event.location
        )
        is RecordEditEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )
        is RecordEditEvent.OnTemperatureChanged -> copy(
            temperatureEditorModel = event.temperatureEditorModel
        )
        is RecordEditEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )
        else -> TODO()
    }
}