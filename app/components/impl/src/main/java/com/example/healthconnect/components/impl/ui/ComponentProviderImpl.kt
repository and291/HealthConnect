package com.example.healthconnect.components.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorComponent
import com.example.healthconnect.components.impl.ui.model.TimeEditorInternalModel
import java.time.Instant
import java.time.ZoneOffset

internal class ComponentProviderImpl : ComponentProvider {

    @Composable
    override fun TimeEditor(
        time: Instant,
        zoneOffset: ZoneOffset?,
        modifier: Modifier,
        onChanged: (TimeEditorModel) -> Unit,
    ) = TimeEditorComponent(
        model = TimeEditorInternalModel.create(time, zoneOffset),
        onTimeChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun MetadataEditor(
        metadataEditorModel: MetadataEditorModel,
        modifier: Modifier,
        onChanged: (MetadataEditorModel) -> Unit,
    ) = MetadataEditorComponent(
        metadataEditorModel = metadataEditorModel,
        onMetadataChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun DoubleValueEditor(
        editorModel: DoubleValueEditorModel,
        modifier: Modifier,
        onChanged: (DoubleValueEditorModel) -> Unit,
    ) = DoubleValueEditorComponent(
        editorModel = editorModel,
        onChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun Selector(
        editor: SelectorEditorModel,
        modifier: Modifier,
        onChanged: (SelectorEditorModel) -> Unit,
    ): Unit = SelectorComponent(
        editor = editor,
        onItemSelected = { onChanged(it) }
    )
}