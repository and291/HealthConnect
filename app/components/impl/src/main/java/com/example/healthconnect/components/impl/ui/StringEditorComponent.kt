package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.ui.model.StringComponentModel

@Composable
internal fun StringEditorComponent(
    model: StringComponentModel,
    onChanged: (StringComponentModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = model.value,
        onValueChange = {
            onChanged(model.copy(value = it))
        },
        label = {
            Text(model.type.label)
        },
        supportingText = {
            Text(model.type.supportingText)
        },
        modifier = modifier.fillMaxWidth()
    )
}