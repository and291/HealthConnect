package com.example.healthconnect.components.impl.ui.editor.atomic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseCompletionGoalField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCompletionGoalFieldEditor(
    model: ExerciseCompletionGoalField,
    onChanged: (ExerciseCompletionGoalField) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val goalTypes = listOf(
        "Distance", "Duration", "Repetitions", "Steps",
        "Active Calories Burned", "Total Energy Burned", "Manual Completion"
    )

    val currentGoalType = when (model) {
        is ExerciseCompletionGoalField.Distance -> "Distance"
        is ExerciseCompletionGoalField.Duration -> "Duration"
        is ExerciseCompletionGoalField.Repetitions -> "Repetitions"
        is ExerciseCompletionGoalField.Steps -> "Steps"
        is ExerciseCompletionGoalField.ActiveCaloriesBurned -> "Active Calories Burned"
        is ExerciseCompletionGoalField.TotalEnergyBurned -> "Total Energy Burned"
        is ExerciseCompletionGoalField.ManualCompletion -> "Manual Completion"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text("Completion Goal", style = MaterialTheme.typography.labelLarge)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = currentGoalType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Goal Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                goalTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            if (type != currentGoalType) {
                                val newGoal = when (type) {
                                    "Distance" -> ExerciseCompletionGoalField.Distance(0.0, model.instanceId)
                                    "Duration" -> ExerciseCompletionGoalField.Duration(0L, model.instanceId)
                                    "Repetitions" -> ExerciseCompletionGoalField.Repetitions(0, model.instanceId)
                                    "Steps" -> ExerciseCompletionGoalField.Steps(0, model.instanceId)
                                    "Active Calories Burned" -> ExerciseCompletionGoalField.ActiveCaloriesBurned(0.0, model.instanceId)
                                    "Total Energy Burned" -> ExerciseCompletionGoalField.TotalEnergyBurned(0.0, model.instanceId)
                                    else -> ExerciseCompletionGoalField.ManualCompletion(model.instanceId)
                                }
                                onChanged(newGoal)
                            }
                            expanded = false
                        }
                    )
                }
            }
        }

        // Specific editors based on type
        when (model) {
            is ExerciseCompletionGoalField.Distance -> {
                OutlinedTextField(
                    value = model.meters.toString(),
                    onValueChange = { onChanged(model.copy(meters = it.toDoubleOrNull() ?: 0.0)) },
                    label = { Text("Meters") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            is ExerciseCompletionGoalField.Duration -> {
                OutlinedTextField(
                    value = model.seconds.toString(),
                    onValueChange = { onChanged(model.copy(seconds = it.toLongOrNull() ?: 0L)) },
                    label = { Text("Seconds") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            is ExerciseCompletionGoalField.Repetitions -> {
                OutlinedTextField(
                    value = model.count.toString(),
                    onValueChange = { onChanged(model.copy(count = it.toIntOrNull() ?: 0)) },
                    label = { Text("Count") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            is ExerciseCompletionGoalField.Steps -> {
                OutlinedTextField(
                    value = model.count.toString(),
                    onValueChange = { onChanged(model.copy(count = it.toIntOrNull() ?: 0)) },
                    label = { Text("Count") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            is ExerciseCompletionGoalField.ActiveCaloriesBurned -> {
                OutlinedTextField(
                    value = model.kilocalories.toString(),
                    onValueChange = { onChanged(model.copy(kilocalories = it.toDoubleOrNull() ?: 0.0)) },
                    label = { Text("Kilocalories") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            is ExerciseCompletionGoalField.TotalEnergyBurned -> {
                OutlinedTextField(
                    value = model.kilocalories.toString(),
                    onValueChange = { onChanged(model.copy(kilocalories = it.toDoubleOrNull() ?: 0.0)) },
                    label = { Text("Kilocalories") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
            is ExerciseCompletionGoalField.ManualCompletion -> {
                // No extra fields
            }
        }
    }
}

@Preview(showBackground = true, name = "Distance")
@Composable
private fun DistancePreview() {
    ExerciseCompletionGoalFieldEditor(
        model = ExerciseCompletionGoalField.Distance(meters = 1000.0),
        onChanged = {}
    )
}

@Preview(showBackground = true, name = "Duration")
@Composable
private fun DurationPreview() {
    ExerciseCompletionGoalFieldEditor(
        model = ExerciseCompletionGoalField.Duration(seconds = 3600L),
        onChanged = {}
    )
}

@Preview(showBackground = true, name = "Repetitions")
@Composable
private fun RepetitionsPreview() {
    ExerciseCompletionGoalFieldEditor(
        model = ExerciseCompletionGoalField.Repetitions(count = 10),
        onChanged = {}
    )
}

@Preview(showBackground = true, name = "Steps")
@Composable
private fun StepsPreview() {
    ExerciseCompletionGoalFieldEditor(
        model = ExerciseCompletionGoalField.Steps(count = 5000),
        onChanged = {}
    )
}

@Preview(showBackground = true, name = "Active Calories Burned")
@Composable
private fun ActiveCaloriesBurnedPreview() {
    ExerciseCompletionGoalFieldEditor(
        model = ExerciseCompletionGoalField.ActiveCaloriesBurned(kilocalories = 500.0),
        onChanged = {}
    )
}

@Preview(showBackground = true, name = "Total Energy Burned")
@Composable
private fun TotalEnergyBurnedPreview() {
    ExerciseCompletionGoalFieldEditor(
        model = ExerciseCompletionGoalField.TotalEnergyBurned(kilocalories = 2000.0),
        onChanged = {}
    )
}

@Preview(showBackground = true, name = "Manual Completion")
@Composable
private fun ManualCompletionPreview() {
    ExerciseCompletionGoalFieldEditor(
        model = ExerciseCompletionGoalField.ManualCompletion(),
        onChanged = {}
    )
}
