package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BloodGlucoseModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val level: ValueComponentModel,
    val specimenSource: SelectorComponentModel,
    val mealType: SelectorComponentModel,
    val relationToMeals: SelectorComponentModel,
) : Instantaneous()