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
}