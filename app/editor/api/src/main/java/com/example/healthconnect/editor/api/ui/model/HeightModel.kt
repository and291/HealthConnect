package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class HeightModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val height: DoubleValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            height is DoubleValueComponentModel.Valid &&
            metadata.isValid()
}