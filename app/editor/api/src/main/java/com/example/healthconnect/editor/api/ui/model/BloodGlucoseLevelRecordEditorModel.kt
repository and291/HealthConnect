package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BloodGlucoseLevelRecordEditorModel(
    val time: TimeEditorModel,
    override val metadata: MetadataEditorModel,
    val level: DoubleValueEditorModel,
    val specimenSource: SelectorEditorModel,
    val mealType: SelectorEditorModel,
    val relationToMeals: SelectorEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = time is TimeEditorModel.Valid &&
            level is DoubleValueEditorModel.Valid &&
            metadata.isValid()
}