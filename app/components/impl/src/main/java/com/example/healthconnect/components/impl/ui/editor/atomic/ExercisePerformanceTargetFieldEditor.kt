package com.example.healthconnect.components.impl.ui.editor.atomic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExercisePerformanceTargetField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisePerformanceTargetFieldEditor(
    model: ExercisePerformanceTargetField,
    onChanged: (ExercisePerformanceTargetField) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val targetTypes = listOf("Heart Rate", "Power", "Speed", "Cadence", "Weight")

    val currentTargetType = when (model) {
        is ExercisePerformanceTargetField.HeartRate -> "Heart Rate"
        is ExercisePerformanceTargetField.Power -> "Power"
        is ExercisePerformanceTargetField.Speed -> "Speed"
        is ExercisePerformanceTargetField.Cadence -> "Cadence"
        is ExercisePerformanceTargetField.Weight -> "Weight"
    }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = currentTargetType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Target Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    targetTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                if (type != currentTargetType) {
                                    val newTarget = when (type) {
                                        "Heart Rate" -> ExercisePerformanceTargetField.HeartRate(0.0, 0.0, model.instanceId)
                                        "Power" -> ExercisePerformanceTargetField.Power(0.0, 0.0, model.instanceId)
                                        "Speed" -> ExercisePerformanceTargetField.Speed(0.0, 0.0, model.instanceId)
                                        "Cadence" -> ExercisePerformanceTargetField.Cadence(0.0, 0.0, model.instanceId)
                                        else -> ExercisePerformanceTargetField.Weight(0.0, model.instanceId)
                                    }
                                    onChanged(newTarget)
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Target")
            }
        }

        when (model) {
            is ExercisePerformanceTargetField.HeartRate -> {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = model.minBpm.toString(),
                        onValueChange = { onChanged(model.copy(minBpm = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Min BPM") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    )
                    OutlinedTextField(
                        value = model.maxBpm.toString(),
                        onValueChange = { onChanged(model.copy(maxBpm = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Max BPM") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }
            }
            is ExercisePerformanceTargetField.Power -> {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = model.minWatts.toString(),
                        onValueChange = { onChanged(model.copy(minWatts = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Min Watts") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    )
                    OutlinedTextField(
                        value = model.maxWatts.toString(),
                        onValueChange = { onChanged(model.copy(maxWatts = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Max Watts") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }
            }
            is ExercisePerformanceTargetField.Speed -> {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = model.minMetersPerSecond.toString(),
                        onValueChange = { onChanged(model.copy(minMetersPerSecond = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Min m/s") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    )
                    OutlinedTextField(
                        value = model.maxMetersPerSecond.toString(),
                        onValueChange = { onChanged(model.copy(maxMetersPerSecond = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Max m/s") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }
            }
            is ExercisePerformanceTargetField.Cadence -> {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    OutlinedTextField(
                        value = model.minRpm.toString(),
                        onValueChange = { onChanged(model.copy(minRpm = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Min RPM") },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    )
                    OutlinedTextField(
                        value = model.maxRpm.toString(),
                        onValueChange = { onChanged(model.copy(maxRpm = it.toDoubleOrNull() ?: 0.0)) },
                        label = { Text("Max RPM") },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                }
            }
            is ExercisePerformanceTargetField.Weight -> {
                OutlinedTextField(
                    value = model.massKg.toString(),
                    onValueChange = { onChanged(model.copy(massKg = it.toDoubleOrNull() ?: 0.0)) },
                    label = { Text("Mass KG") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        }
    }
}
