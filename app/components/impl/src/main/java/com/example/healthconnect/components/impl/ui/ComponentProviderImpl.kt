package com.example.healthconnect.components.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
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
        onChanged: (TimeComponentModel) -> Unit,
    ) = TimeEditorComponent(
        model = TimeEditorInternalModel.create(time, zoneOffset),
        onChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun MetadataEditor(
        metadata: MetadataComponentModel,
        modifier: Modifier,
        onChanged: (MetadataComponentModel) -> Unit,
    ) = MetadataEditorComponent(
        model = metadata,
        onChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun DoubleValueEditor(
        value: ValueComponentModel,
        modifier: Modifier,
        onChanged: (ValueComponentModel) -> Unit,
    ) = ValueEditorComponent(
        model = value,
        onChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun Selector(
        selector: SelectorComponentModel,
        modifier: Modifier,
        onChanged: (SelectorComponentModel) -> Unit,
    ): Unit = SelectorComponent(
        editor = selector,
        onItemSelected = { onChanged(it) }
    )
}