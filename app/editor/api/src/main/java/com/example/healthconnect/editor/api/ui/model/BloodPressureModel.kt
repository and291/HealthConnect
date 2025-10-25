package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BloodPressureModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val systolic: DoubleValueComponentModel,
    val diastolic: DoubleValueComponentModel,
    val bodyPosition: SelectorComponentModel,
    val measurementLocation: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            systolic is DoubleValueComponentModel.Valid &&
            diastolic is DoubleValueComponentModel.Valid &&
            bodyPosition is SelectorComponentModel.Valid &&
            measurementLocation is SelectorComponentModel.Valid &&
            metadata.isValid()
}