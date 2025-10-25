package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BloodGlucoseModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val level: DoubleValueComponentModel,
    val specimenSource: SelectorComponentModel,
    val mealType: SelectorComponentModel,
    val relationToMeals: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            level is DoubleValueComponentModel.Valid &&
            metadata.isValid()
}