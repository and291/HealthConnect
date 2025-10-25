package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BasalMetabolicRateRecordEditorModel(
    val time: TimeEditorModel,
    val power: DoubleValueEditorModel,
    override val metadata: MetadataEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = time is TimeEditorModel.Valid &&
            power is DoubleValueEditorModel.Valid &&
            metadata.isValid()
}