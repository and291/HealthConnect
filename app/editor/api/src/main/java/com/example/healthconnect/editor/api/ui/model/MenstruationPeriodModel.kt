package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class MenstruationPeriodModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
) : Interval()