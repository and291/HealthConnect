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

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun update(event: RecordModificationEvent): RecordEditorModel = when (event) {
        is RecordModificationEvent.OnValueSelected -> when (event.selector.type) {
            is SelectorEditorModel.Type.MealType -> copy(
                mealType = event.selector,
            )

            is SelectorEditorModel.Type.RelationToMeal -> copy(
                relationToMeals = event.selector,
            )

            is SelectorEditorModel.Type.SpecimenSource -> copy(
                specimenSource = event.selector,
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnMetadataChanged -> copy(
            metadata = event.metadata
        )

        is RecordModificationEvent.OnDoubleValueChanged -> when (event.value.type) {
            is DoubleValueEditorModel.Type.BloodGlucoseLevel -> copy(
                level = event.value
            )

            else -> throw NotImplementedError()
        }

        is RecordModificationEvent.OnTimeChanged -> copy(
            time = event.time
        )

        else -> throw NotImplementedError()
    }

}