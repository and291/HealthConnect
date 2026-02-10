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
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.impl.ui.editor.TimeEditor
import java.time.Instant

@Composable
internal fun ExerciseRouteFieldEditor(
    location: ExerciseRouteField,
    onChanged: (ExerciseRouteField) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Location", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
        TimeEditor(
            labelText = "Time",
            instant = location.time,
            onChanged = { onChanged(location.copy(time = it)) }
        )
        OutlinedTextField(
            value = location.latitude.toString(),
            onValueChange = { newValue ->
                newValue.toDoubleOrNull()?.let {
                    onChanged(location.copy(latitude = it))
                }
            },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location.longitude.toString(),
            onValueChange = { newValue ->
                newValue.toDoubleOrNull()?.let {
                    onChanged(location.copy(longitude = it))
                }
            },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location.altitude?.toString() ?: "",
            onValueChange = { newValue ->
                onChanged(location.copy(altitude = newValue.toDoubleOrNull()))
            },
            label = { Text("Altitude (m)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location.horizontalAccuracy?.toString() ?: "",
            onValueChange = { newValue ->
                onChanged(location.copy(horizontalAccuracy = newValue.toDoubleOrNull()))
            },
            label = { Text("Horizontal Accuracy (m)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = location.verticalAccuracy?.toString() ?: "",
            onValueChange = { newValue ->
                onChanged(location.copy(verticalAccuracy = newValue.toDoubleOrNull()))
            },
            label = { Text("Vertical Accuracy (m)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExerciseRouteFieldEditorPreview() {
    ExerciseRouteFieldEditor(
        location = ExerciseRouteField(
            time = Instant.now(),
            latitude = 37.7749,
            longitude = -122.4194,
            altitude = 10.0,
            horizontalAccuracy = 5.0,
            verticalAccuracy = 2.0
        ),
        onChanged = {},
        onDelete = {}
    )
}
