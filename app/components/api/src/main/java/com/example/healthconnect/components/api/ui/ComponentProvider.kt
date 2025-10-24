package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import java.time.Instant
import java.time.ZoneOffset

interface ComponentProvider {

    @Composable
    fun TimeEditor(
        time: Instant,
        zoneOffset: ZoneOffset?,
        modifier: Modifier,
        onChanged: (TimeEditorModel) -> Unit,
    )

    @Composable
    fun MetadataEditor(
        metadataEditorModel: MetadataEditorModel,
        modifier: Modifier,
        onChanged: (MetadataEditorModel) -> Unit,
    )

    @Composable
    fun DoubleValueEditor(
        editorModel: DoubleValueEditorModel,
        modifier: Modifier,
        onChanged: (DoubleValueEditorModel) -> Unit,
    )

    @Composable
    fun Selector(
        editor: SelectorEditorModel,
        modifier: Modifier,
        onChanged: (SelectorEditorModel) -> Unit,
    )
}