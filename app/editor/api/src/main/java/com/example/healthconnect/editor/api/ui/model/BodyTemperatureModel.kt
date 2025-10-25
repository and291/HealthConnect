package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

data class BodyTemperatureModel(
    val time: TimeComponentModel,
    override val metadata: MetadataComponentModel,
    val temperature: DoubleValueComponentModel,
    val measurementLocation: SelectorComponentModel,
) : Model() {

    override fun isValid(): Boolean = time is TimeComponentModel.Valid &&
            temperature is DoubleValueComponentModel.Valid &&
            measurementLocation is SelectorComponentModel.Valid &&
            metadata.isValid()
}