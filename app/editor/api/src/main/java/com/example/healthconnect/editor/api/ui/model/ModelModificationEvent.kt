package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

sealed class ModelModificationEvent {

    data class OnTimeChanged(
        val time: TimeComponentModel,
    ) : ModelModificationEvent()

    data class OnValueSelected(
        val selector: SelectorComponentModel
    ) : ModelModificationEvent()

    data class OnMetadataChanged(
        val metadata: MetadataComponentModel
    ) : ModelModificationEvent()

    data class OnDoubleValueChanged(
        val value: DoubleValueComponentModel,
    ) : ModelModificationEvent()
}