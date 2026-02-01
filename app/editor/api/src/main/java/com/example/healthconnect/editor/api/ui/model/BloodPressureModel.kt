package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel

data class BloodPressureModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val systolic: ValueComponentModel,
    val diastolic: ValueComponentModel,
    val bodyPosition: SelectorComponentModel,
    val measurementLocation: SelectorComponentModel,
) : Instantaneous()