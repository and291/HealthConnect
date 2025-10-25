package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BasalMetabolicRateModel(
    val time: TimeComponentModel,
    val power: DoubleValueComponentModel,
    override val metadata: MetadataComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            power is DoubleValueComponentModel.Valid &&
            metadata.isValid()
}