package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class RestingHeartRateModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    //User's weight in kilograms. Required field. Valid range: 0-1000 kilograms.
    val beatsPerMinute: ValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            beatsPerMinute is ValueComponentModel.ValidLong &&
            metadata.isValid()
}