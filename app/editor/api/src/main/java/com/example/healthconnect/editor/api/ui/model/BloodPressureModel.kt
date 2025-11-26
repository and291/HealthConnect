package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BloodPressureModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val systolic: ValueComponentModel,
    val diastolic: ValueComponentModel,
    val bodyPosition: SelectorComponentModel,
    val measurementLocation: SelectorComponentModel,
) : Instantaneous()