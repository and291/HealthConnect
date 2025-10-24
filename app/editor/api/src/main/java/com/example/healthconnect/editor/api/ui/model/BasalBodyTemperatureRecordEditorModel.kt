package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BasalBodyTemperatureRecordEditorModel(
    val time: TimeEditorModel,
    override val metadata: MetadataEditorModel,
    val temperature: DoubleValueEditorModel,
    val measurementLocation: SelectorEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = time is TimeEditorModel.Valid &&
            temperature is DoubleValueEditorModel.Valid &&
            measurementLocation is SelectorEditorModel.Valid &&
            metadata.isValid()

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorEditorModel.Type.MeasurementLocationBodyTemperature -> copy(
                measurementLocation = event.selector
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.Temperature -> copy(
                temperature = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnTimeChanged -> copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }
}