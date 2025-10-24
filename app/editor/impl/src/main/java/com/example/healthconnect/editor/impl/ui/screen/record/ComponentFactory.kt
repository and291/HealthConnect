package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.ComponentEditorModel
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordEditorModel
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnDoubleValueChanged
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnMetadataChanged
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnTimeChanged
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent.OnValueSelected

class ComponentFactory(
    private val componentProvider: ComponentProvider,
) {

    @Composable
    fun Create(
        recordEditorModel: RecordEditorModel,
        modifier: Modifier = Modifier,
        eventHandler: (RecordModificationEvent) -> Unit,
    ) = recordEditorModel.getComponents().forEach {
        Create(
            editorModel = it,
            eventHandler = eventHandler,
            modifier = modifier,
        )
    }

    @Composable
    private fun Create(
        editorModel: ComponentEditorModel,
        eventHandler: (RecordModificationEvent) -> Unit,
        modifier: Modifier,
    ) = when (editorModel) {
        is TimeEditorModel.Valid -> componentProvider.TimeEditor(
            time = editorModel.instant,
            zoneOffset = editorModel.zoneOffset,
            modifier = modifier,
        ) { eventHandler(OnTimeChanged(it)) }

        is TimeEditorModel.Invalid -> TODO()

        is MetadataEditorModel -> componentProvider.MetadataEditor(editorModel, modifier) {
            eventHandler(OnMetadataChanged(it))
        }

        is DoubleValueEditorModel -> componentProvider.DoubleValueEditor(editorModel, modifier) {
            eventHandler(OnDoubleValueChanged(it))
        }

        is SelectorEditorModel -> componentProvider.Selector(editorModel, modifier) {
            eventHandler(OnValueSelected(it))
        }
    }
}