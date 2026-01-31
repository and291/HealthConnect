package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.components.api.ui.model.StringComponentModel

@Composable
internal fun StringEditorComponent(
    model: StringComponentModel,
    onChanged: (StringComponentModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = model.value,
        enabled = !model.readOnly,
        onValueChange = {
            if (!model.readOnly) {
                onChanged(model.copy(value = it))
            }
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

@Preview(showBackground = true)
@Composable
fun StringEditorComponentPreview() {
    StringEditorComponent(
        model = StringComponentModel(
            value = "Sample text",
            type = StringComponentModel.Type.MindfulnessSessionTitle()
        ),
        onChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
fun StringEditorComponentReadOnlyPreview() {
    StringEditorComponent(
        model = StringComponentModel(
            value = "Read only text",
            type = StringComponentModel.Type.MindfulnessSessionTitle(),
            readOnly = true
        ),
        onChanged = {}
    )
}
