package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class CervicalMucusModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val appearance: SelectorComponentModel,
    val sensation: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            appearance is SelectorComponentModel.Valid &&
            sensation is SelectorComponentModel.Valid &&
            metadata.isValid()
}