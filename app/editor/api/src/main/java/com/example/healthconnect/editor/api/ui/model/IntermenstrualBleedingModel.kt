package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class IntermenstrualBleedingModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            metadata.isValid()
}