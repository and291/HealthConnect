package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class OvulationTestModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val result: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            result is SelectorComponentModel.Valid &&
            metadata.isValid()
}