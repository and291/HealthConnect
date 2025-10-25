package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BodyFatRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadata: MetadataEditorModel,
    val percentage: DoubleValueEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            percentage is DoubleValueEditorModel.Valid &&
            metadata.isValid()
}