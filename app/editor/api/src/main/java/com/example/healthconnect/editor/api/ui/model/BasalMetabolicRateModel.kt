package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BasalMetabolicRateModel(
    val time: TimeComponentModel,
    val power: ValueComponentModel,
    override val metadata: MetadataComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            power is ValueComponentModel.ValidDouble &&
            metadata.isValid()
}