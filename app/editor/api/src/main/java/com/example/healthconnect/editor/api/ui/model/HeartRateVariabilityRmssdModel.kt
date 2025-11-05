package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class HeartRateVariabilityRmssdModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val heartRateVariabilityMillis: ValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            heartRateVariabilityMillis is ValueComponentModel.ValidDouble &&
            metadata.isValid()
}