package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel

data class BloodGlucoseLevelRecordEditorModel(
    val timeEditorModel: TimeEditorModel,
    override val metadataEditorModel: MetadataEditorModel,
    val level: DoubleValueEditorModel,
    val specimenSource: SelectorEditorModel,
    val mealType: SelectorEditorModel,
    val relationToMeals: SelectorEditorModel,
) : RecordEditorModel() {

    override fun isValid(): Boolean = timeEditorModel is TimeEditorModel.Valid &&
            level is DoubleValueEditorModel.Valid &&
            metadataEditorModel.isValid()

    override fun update(event: RecordEditEvent): RecordEditorModel = when (event) {
        is RecordEditEvent.OnValueSelected -> when (event.editorModel.type) {
            is SelectorEditorModel.Type.MealType -> copy(
                mealType = event.editorModel,
            )

            is SelectorEditorModel.Type.RelationToMeal -> copy(
                relationToMeals = event.editorModel,
            )

            is SelectorEditorModel.Type.SpecimenSource -> copy(
                specimenSource = event.editorModel,
            )

            else -> TODO()
        }

        is RecordEditEvent.OnMetadataChanged -> copy(
            metadataEditorModel = event.metadata
        )

        is RecordEditEvent.OnDoubleValueChanged -> when (event.editorModel.type) {
            is DoubleValueEditorModel.Type.BloodGlucoseLevel -> copy(
                level = event.editorModel
            )

            else -> TODO()
        }

        is RecordEditEvent.OnTimeChanged -> copy(
            timeEditorModel = event.timeEditorModel
        )

        else -> TODO()
    }

}