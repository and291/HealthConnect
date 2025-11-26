package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class RestingHeartRateModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    //Heart beats per minute. Required field. Validation range: 1-300.
    val beatsPerMinute: ValueComponentModel,
) : Instantaneous()