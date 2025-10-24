package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BasalBodyTemperatureRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val temperatureEditorModel: DoubleValueEditorModel,
    val measurementLocation: SelectorEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            temperatureEditorModel is DoubleValueEditorModel.Valid &&
            measurementLocation is SelectorEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnValueSelected -> when (event.editorModel.type) {
            is SelectorEditorModel.Type.BodyTemperatureMeasurementLocationType -> copy(
                measurementLocation = event.editorModel
            )

            else -> TODO()
        }

        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.editorModel.type) {
            is DoubleValueEditorModel.Type.Temperature -> copy(
                temperatureEditorModel = event.editorModel
            )

            else -> TODO()
        }

        is RecordModificationEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )
    }
}