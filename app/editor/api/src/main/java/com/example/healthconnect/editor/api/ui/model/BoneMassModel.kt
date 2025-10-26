package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BoneMassModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val mass: DoubleValueComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            mass is DoubleValueComponentModel.Valid &&
            metadata.isValid()
}