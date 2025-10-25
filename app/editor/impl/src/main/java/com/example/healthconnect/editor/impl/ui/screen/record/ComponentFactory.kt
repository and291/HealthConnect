package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.ComponentModel
import com.example.healthconnect.components.api.ui.model.DoubleValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.editor.api.ui.model.Model
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnDoubleValueChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnMetadataChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnTimeChanged
import com.example.healthconnect.editor.api.ui.model.ModelModificationEvent.OnValueSelected

class ComponentFactory(
    private val componentProvider: ComponentProvider,
) {

    @Composable
    fun Create(
        model: Model,
        modifier: Modifier = Modifier,
        eventHandler: (ModelModificationEvent) -> Unit,
    ) = model.getComponents().forEach {
        Create(
            editorModel = it,
            eventHandler = eventHandler,
            modifier = modifier,
        )
    }

    @Composable
    private fun Create(
        editorModel: ComponentModel,
        eventHandler: (ModelModificationEvent) -> Unit,
        modifier: Modifier,
    ) = when (editorModel) {
        is TimeComponentModel.Valid -> componentProvider.TimeEditor(
            time = editorModel.instant,
            zoneOffset = editorModel.zoneOffset,
            modifier = modifier,
        ) { eventHandler(OnTimeChanged(it)) }

        is TimeComponentModel.Invalid -> TODO()

        is MetadataComponentModel -> componentProvider.MetadataEditor(editorModel, modifier) {
            eventHandler(OnMetadataChanged(it))
        }

        is DoubleValueComponentModel -> componentProvider.DoubleValueEditor(editorModel, modifier) {
            eventHandler(OnDoubleValueChanged(it))
        }

        is SelectorComponentModel -> componentProvider.Selector(editorModel, modifier) {
            eventHandler(OnValueSelected(it))
        }
    }
}