package com.example.healthconnect.components.impl.ui.editor.atomic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.api.domain.entity.field.atomic.HeartRateSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.impl.ui.editor.TimeEditor
import java.time.Instant

@Composable
internal fun HeartRateSampleFieldEditor(
    model: HeartRateSampleField,
    onChanged: (HeartRateSampleField) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Heart Rate Sample", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
        ValueFieldEditor(
            model = model.heartRate,
            onChanged = { onChanged(model.copy(heartRate = it)) },
            modifier = Modifier.fillMaxWidth()
        )
        TimeEditor(
            labelText = "Time",
            instant = model.time,
            onChanged = { onChanged(model.copy(time = it)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeartRateSampleFieldEditorPreview() {
    HeartRateSampleFieldEditor(
        model = HeartRateSampleField(
            time = Instant.now(),
            heartRate = ValueField.Lng(
                parsedValue = 72,
                type = ValueField.Type.BeatsPerMinute()
            )
        ),
        onChanged = {},
        onDelete = {}
    )
}
