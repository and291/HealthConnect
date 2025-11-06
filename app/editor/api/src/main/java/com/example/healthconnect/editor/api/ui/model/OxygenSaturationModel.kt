package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class OxygenSaturationModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val percentage: ValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            percentage is ValueComponentModel.ValidDouble &&
            metadata.isValid()
}