package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class Vo2MaxModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val vo2MillilitersPerMinuteKilogram: DoubleValueComponentModel,
    val measurementMethod: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            vo2MillilitersPerMinuteKilogram is DoubleValueComponentModel.Valid &&
            measurementMethod is SelectorComponentModel.Valid &&
            metadata.isValid()
}