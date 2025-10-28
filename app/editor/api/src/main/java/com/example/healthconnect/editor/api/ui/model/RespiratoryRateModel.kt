package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class RespiratoryRateModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    //Respiratory rate in breaths per minute. Required field. Valid range: 0-1000.
    val rate: DoubleValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            rate is DoubleValueComponentModel.Valid &&
            metadata.isValid()
}