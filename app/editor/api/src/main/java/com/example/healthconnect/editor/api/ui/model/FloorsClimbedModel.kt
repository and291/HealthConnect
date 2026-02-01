package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.top.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel

data class FloorsClimbedModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val floors: ValueComponentModel,
) : Interval()