package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.StringComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.top.ListComponentModel
import com.example.healthconnect.editor.api.ui.model.Model
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnValueChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnMetadataChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnTimeChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnValueSelected
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnStringValueChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnListChanged

class ComponentFactory(
    private val provider: ComponentProvider,
) {

    fun LazyListScope.create(
        model: Model,
        modifier: Modifier = Modifier,
        eventHandler: (ModelModificationEvent) -> Unit,
    ) {
        model.getComponents().forEach { componentModel ->
            when (componentModel) {
                is TimeComponentModel -> item(key = componentModel.presentationId) {
                    provider.TimeEditor(
                        time = componentModel,
                        modifier = modifier,
                    ) { eventHandler(OnTimeChanged(it)) }
                }

                is MetadataComponentModel -> with(provider) {
                    metadataEditor(
                        metadata = componentModel,
                    ) { eventHandler(OnMetadataChanged(it)) }
                }

                is ValueComponentModel -> item(key = componentModel.presentationId) {
                    provider.ValueEditor(
                        value = componentModel,
                        modifier = modifier
                    ) { eventHandler(OnValueChanged(it)) }
                }

                is SelectorComponentModel -> item(key = componentModel.presentationId) {
                    provider.Selector(
                        selector = componentModel,
                        modifier = modifier
                    ) { eventHandler(OnValueSelected(it)) }
                }

                is StringComponentModel -> item(key = componentModel.presentationId) {
                    provider.StringEditor(
                        value = componentModel,
                        modifier = modifier
                    ) { eventHandler(OnStringValueChanged(it)) }
                }

                is ListComponentModel<*> -> with(provider) {
                    listEditor(
                        model = componentModel,
                        modifier = modifier
                    ) { eventHandler(OnListChanged(it)) }
                }
            }
        }
    }
}
