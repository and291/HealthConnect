package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.ComponentModel
import com.example.healthconnect.components.api.ui.model.top.ListComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.StringComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel
import com.example.healthconnect.components.impl.ui.metadata.metadataEditorItems

internal class ComponentProviderImpl : ComponentProvider {

    @Composable
    override fun TimeEditor(
        time: TimeComponentModel,
        modifier: Modifier,
        onChanged: (TimeComponentModel) -> Unit,
    ) = TimeEditorComponent(
        model = time,
        onChanged = onChanged,
        modifier = modifier,
    )

    override fun LazyListScope.metadataEditor(
        metadata: MetadataComponentModel,
        onChanged: (MetadataComponentModel) -> Unit,
    ) = metadataEditorItems(
        model = metadata,
        onChanged = onChanged
    )

    @Composable
    override fun ValueEditor(
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

    @Composable
    override fun StringEditor(
        value: StringComponentModel,
        modifier: Modifier,
        onChanged: (StringComponentModel) -> Unit,
    ) = StringEditorComponent(
        model = value,
        onChanged = onChanged,
        modifier = modifier
    )

    override fun <T : ComponentModel> LazyListScope.listEditor(
        model: ListComponentModel<T>,
        modifier: Modifier,
        onChanged: (ListComponentModel<T>) -> Unit,
    ) = listEditorItems(
        model = model,
        modifier = modifier,
        onChanged = onChanged
    )
}
