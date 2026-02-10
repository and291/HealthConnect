package com.example.healthconnect.components.impl.ui.editor.atomic

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField

@Composable
internal fun StringFieldEditor(
    model: StringField,
    onChanged: (StringField) -> Unit,
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
fun StringFieldEditorPreview() {
    StringFieldEditor(
        model = StringField(
            value = "Sample text",
            type = StringField.Type.MindfulnessSessionTitle()
        ),
        onChanged = {}
    )
}

@Preview(showBackground = true)
@Composable
fun StringFieldEditorReadOnlyPreview() {
    StringFieldEditor(
        model = StringField(
            value = "Read only text",
            type = StringField.Type.MindfulnessSessionTitle(),
            readOnly = true
        ),
        onChanged = {}
    )
}
