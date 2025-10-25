package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BloodPressureRecordEditorModel(
    val time: TimeEditorModel,
    override val metadata: MetadataEditorModel,
    val systolic: DoubleValueEditorModel,
    val diastolic: DoubleValueEditorModel,
    val bodyPosition: SelectorEditorModel,
    val measurementLocation: SelectorEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = time is TimeEditorModel.Valid &&
            systolic is DoubleValueEditorModel.Valid &&
            diastolic is DoubleValueEditorModel.Valid &&
            bodyPosition is SelectorEditorModel.Valid &&
            measurementLocation is SelectorEditorModel.Valid &&
            metadata.isValid()
}