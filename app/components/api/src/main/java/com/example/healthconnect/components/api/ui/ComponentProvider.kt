package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel
import com.example.healthconnect.components.api.ui.model.SelectorComponentModel
import com.example.healthconnect.components.api.ui.model.TimeComponentModel

interface ComponentProvider {

    @Composable
    fun TimeEditor(
        time: TimeComponentModel,
        modifier: Modifier,
        onChanged: (TimeComponentModel) -> Unit,
    )

    @Composable
    fun MetadataEditor(
        metadata: MetadataComponentModel,
        modifier: Modifier,
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
}