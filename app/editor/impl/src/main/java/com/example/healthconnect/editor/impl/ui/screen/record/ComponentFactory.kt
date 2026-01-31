package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.StringComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.model.Model
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnValueChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnMetadataChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnTimeChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnValueSelected
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnStringValueChanged

class ComponentFactory(
    private val provider: ComponentProvider,
) {

    fun LazyListScope.create(
        model: Model,
        modifier: Modifier = Modifier,
        eventHandler: (ModelModificationEvent) -> Unit,
    ) {
        model.getComponents().forEach { componentModel ->
            item(key = componentModel.presentationId) {
                when (componentModel) {
                    is TimeComponentModel -> provider.TimeEditor(
                        time = componentModel,
                        modifier = modifier,
                    ) { eventHandler(OnTimeChanged(it)) }

                    is MetadataComponentModel -> provider.MetadataEditor(
                        metadata = componentModel,
                        modifier = modifier,
                    ) { eventHandler(OnMetadataChanged(it)) }

                    is ValueComponentModel -> provider.ValueEditor(
                        value = componentModel,
                        modifier = modifier
                    ) { eventHandler(OnValueChanged(it)) }

                    is SelectorComponentModel -> provider.Selector(
                        selector = componentModel,
                        modifier = modifier
                    ) { eventHandler(OnValueSelected(it)) }

                    is StringComponentModel -> provider.StringEditor(
                        value = componentModel,
                        modifier = modifier
                    ) { eventHandler(OnStringValueChanged(it)) }
                }
            }
        }
    }
}
