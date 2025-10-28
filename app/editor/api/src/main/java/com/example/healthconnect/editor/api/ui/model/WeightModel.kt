package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class WeightModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    //User's weight in kilograms. Required field. Valid range: 0-1000 kilograms.
    val weight: DoubleValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            weight is DoubleValueComponentModel.Valid &&
            metadata.isValid()
}