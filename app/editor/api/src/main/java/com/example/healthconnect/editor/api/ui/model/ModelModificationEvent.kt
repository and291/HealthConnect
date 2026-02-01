package com.example.healthconnect.editor.api.ui.model

import com.example.healthconnect.components.api.ui.model.top.ListComponentModel
import com.example.healthconnect.components.api.ui.model.top.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.StringComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel

sealed class ModelModificationEvent {
    data class OnTimeChanged(val time: TimeComponentModel) : ModelModificationEvent()
    data class OnMetadataChanged(val metadata: MetadataComponentModel) : ModelModificationEvent()
    data class OnValueChanged(val value: ValueComponentModel) : ModelModificationEvent()
    data class OnValueSelected(val selector: SelectorComponentModel) : ModelModificationEvent()
    data class OnStringValueChanged(val value: StringComponentModel) : ModelModificationEvent()
    data class OnListChanged(val list: ListComponentModel<*>) : ModelModificationEvent()
}
