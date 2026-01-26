package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.ValueComponentModel

data class FloorsClimbedModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val floors: ValueComponentModel,
) : Interval()