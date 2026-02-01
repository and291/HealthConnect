package com.example.healthconnect.components.api.ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.model.ComponentModel
import com.example.healthconnect.components.api.ui.model.top.ListComponentModel
import com.example.healthconnect.components.api.ui.model.top.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.top.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.top.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.top.StringComponentModel
import com.example.healthconnect.components.api.ui.model.top.TimeComponentModel

interface ComponentProvider {

    @Composable
    fun TimeEditor(
        time: TimeComponentModel,
        modifier: Modifier,
        onChanged: (TimeComponentModel) -> Unit,
    )

    fun LazyListScope.metadataEditor(
        metadata: MetadataComponentModel,
        onChanged: (MetadataComponentModel) -> Unit,
    )

    @Composable
    fun ValueEditor(
        value: ValueComponentModel,
        modifier: Modifier,
        onChanged: (ValueComponentModel) -> Unit,
    )

    @Composable
    fun Selector(
        selector: SelectorComponentModel,
        modifier: Modifier,
        onChanged: (SelectorComponentModel) -> Unit,
    )

    @Composable
    fun StringEditor(
        value: StringComponentModel,
        modifier: Modifier,
        onChanged: (StringComponentModel) -> Unit,
    )

    fun <T : ComponentModel> LazyListScope.listEditor(
        model: ListComponentModel<T>,
        modifier: Modifier,
        onChanged: (ListComponentModel<T>) -> Unit,
    )
}
