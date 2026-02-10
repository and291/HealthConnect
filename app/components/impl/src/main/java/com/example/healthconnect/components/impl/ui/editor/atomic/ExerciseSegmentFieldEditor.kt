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
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.impl.ui.editor.TimeEditor
import java.time.Instant

@Composable
internal fun ExerciseSegmentFieldEditor(
    model: ExerciseSegmentField,
    onChanged: (ExerciseSegmentField) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Segment", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }

        val selector = SelectorField(
            value = model.segmentType,
            type = SelectorField.Type.ExerciseSegmentType()
        )
        SelectorFieldEditor(
            editor = selector,
            onItemSelected = { onChanged(model.copy(segmentType = it.value)) }
        )

        OutlinedTextField(
            value = model.repetitions.toString(),
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let {
                    onChanged(model.copy(repetitions = it))
                }
            },
            label = { Text("Repetitions") },
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
private fun ExerciseSegmentFieldEditorPreview() {
    ExerciseSegmentFieldEditor(
        model = ExerciseSegmentField(
            startTime = Instant.now(),
            endTime = Instant.now().plusSeconds(3600),
            segmentType = 1,
            repetitions = 10
        ),
        onChanged = {},
        onDelete = {}
    )
}
