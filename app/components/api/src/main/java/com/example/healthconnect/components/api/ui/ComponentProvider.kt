package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import java.time.Instant
import java.time.ZoneOffset

interface ComponentProvider {

    @Composable
    fun TimeEditor(time: Instant, zoneOffset: ZoneOffset?, onTimeChanged: (TimeEditorModel) -> Unit)

    @Composable
    fun MetadataEditor(
        metadataEditorModel: MetadataEditorModel,
        onMetadataChanged: (MetadataEditorModel) -> Unit
    )

    @Composable
    fun DoubleValueEditor(
        editorModel: DoubleValueEditorModel,
        onChanged: (DoubleValueEditorModel) -> Unit
    )

    @Composable
    fun Selector(
        editor: SelectorEditorModel,
        onLocationChanged: (SelectorEditorModel) -> Unit,
    )
}