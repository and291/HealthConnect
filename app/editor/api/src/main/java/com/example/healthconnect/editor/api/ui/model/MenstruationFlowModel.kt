package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class MenstruationFlowModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val flow: SelectorComponentModel,
) : Instantaneous()