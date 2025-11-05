package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BasalBodyTemperatureModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val temperature: ValueComponentModel,
    val measurementLocation: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            temperature is ValueComponentModel.ValidDouble &&
            measurementLocation is SelectorComponentModel.Valid &&
            metadata.isValid()
}