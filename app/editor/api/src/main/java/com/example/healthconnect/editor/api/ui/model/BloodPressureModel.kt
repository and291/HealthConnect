package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BloodPressureModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val systolic: ValueComponentModel,
    val diastolic: ValueComponentModel,
    val bodyPosition: SelectorComponentModel,
    val measurementLocation: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            systolic is ValueComponentModel.ValidDouble &&
            diastolic is ValueComponentModel.ValidDouble &&
            bodyPosition is SelectorComponentModel.Valid &&
            measurementLocation is SelectorComponentModel.Valid &&
            metadata.isValid()
}