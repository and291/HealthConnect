package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BoneMassModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val mass: ValueComponentModel,
) : Instantaneous() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            mass is ValueComponentModel.ValidDouble &&
            metadata.isValid()
}