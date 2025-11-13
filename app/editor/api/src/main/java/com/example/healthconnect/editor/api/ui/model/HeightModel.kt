package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class HeightModel(
    override val metadata: MetadataComponentModel,
    override val time: TimeComponentModel,
    val height: ValueComponentModel,
) : Instantaneous() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            height is ValueComponentModel.ValidDouble &&
            metadata.isValid()
}