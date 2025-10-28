package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class HeartRateVariabilityRmssdModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val heartRateVariabilityMillis: DoubleValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            heartRateVariabilityMillis is DoubleValueComponentModel.Valid &&
            metadata.isValid()
}