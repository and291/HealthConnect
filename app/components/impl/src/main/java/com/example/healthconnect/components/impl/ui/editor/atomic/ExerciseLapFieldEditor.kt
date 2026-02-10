package com.example.healthconnect.components.impl.ui.editor.atomic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.impl.ui.editor.TimeEditor
import java.time.Instant

@Composable
internal fun ExerciseLapFieldEditor(
    model: ExerciseLapField,
    onChanged: (ExerciseLapField) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Lap", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
        OutlinedTextField(
            value = model.lengthInMeters?.toString() ?: "",
            onValueChange = { newValue ->
                onChanged(model.copy(lengthInMeters = newValue.toDoubleOrNull()))
            },
            label = { Text("Length (m)") },
            modifier = Modifier.fillMaxWidth()
        )
        TimeEditor(
            labelText = "Start time",
            instant = model.startTime,
            onChanged = { onChanged(model.copy(startTime = it)) }
        )
        TimeEditor(
            labelText = "End time",
            instant = model.endTime,
            onChanged = { onChanged(model.copy(endTime = it)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseLapFieldEditorPreview() {
    ExerciseLapFieldEditor(
        model = ExerciseLapField(
            startTime = Instant.now(),
            endTime = Instant.now().plusSeconds(600),
            lengthInMeters = 400.0
        ),
        onChanged = {},
        onDelete = {}
    )
}
