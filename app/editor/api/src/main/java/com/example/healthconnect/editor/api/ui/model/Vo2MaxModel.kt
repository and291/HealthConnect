package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class Vo2MaxModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val vo2MillilitersPerMinuteKilogram: ValueComponentModel,
    val measurementMethod: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            vo2MillilitersPerMinuteKilogram is ValueComponentModel.ValidDouble &&
            measurementMethod is SelectorComponentModel.Valid &&
            metadata.isValid()
}