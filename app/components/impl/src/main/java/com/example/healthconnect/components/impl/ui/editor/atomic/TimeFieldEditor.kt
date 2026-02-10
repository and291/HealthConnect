package com.example.healthconnect.components.impl.ui.editor.atomic

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.Time
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.editor.atomic.TimeFieldEditorViewModel.Event
import java.time.Instant
import java.util.TimeZone

@Composable
internal fun TimeFieldEditor(
    model: TimeField,
    onChanged: (TimeField) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimeFieldEditorViewModel = viewModel(
        factory = Di.editorViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(TimeFieldEditorViewModel.TIME_MODEL_KEY, model)
        }
    ),
) {

    LaunchedEffect(viewModel.state) {
        Log.d(this::class.simpleName, "Time: ${viewModel.state}")
        onChanged(viewModel.state)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        when (val model = viewModel.state) {
            is TimeField.Instantaneous -> {
                TimeComponent(
                    labelText = "Time",
                    time = model.time,
                ) { time, zone ->
                    val event = Event.OnInstantaneousTimeChanged(
                        value = time,
                        zoneId = zone
                    )
                    viewModel.onEvent(event)
                }
            }

            is TimeField.Interval -> {
                TimeComponent(
                    labelText = "Start time",
                    time = model.start,
                ) { time, zone ->
                    val event = Event.OnStartTimeChanged(
                        value = time,
                        zoneId = zone
                    )
                    viewModel.onEvent(event)
                }

                TimeComponent(
                    labelText = "End time",
                    time = model.end,
                ) { time, zone ->
                    val event = Event.OnEndTimeChanged(
                        value = time,
                        zoneId = zone
                    )
                    viewModel.onEvent(event)
                }
            }
        }
    }
}

@Composable
private fun TimeComponent(
    labelText: String,
    time: Time,
    modifier: Modifier = Modifier,
    onChanged: (String, String?) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = time.input,
            isError = time is Time.Invalid,
            label = { Text(labelText) },
            supportingText = {
                //TODO отобразить старое и новое значение в локали юзера, чтобы ему легче было оринетироваться в формате ISO_DATE_TIME если он с ним не знаком
                time.zonedLocalizedTime?.let {
                    Text(it)
                }
            },
            onValueChange = {
                onChanged(it, time.zoneOffset?.id)
            },
        )

        SelectorFieldEditor(
            title = "Zone",
            //TODO teach selector to display errors
            supportText = "",
            selectedText = time.zoneOffset?.id ?: "set",
            items = TimeZone.getAvailableIDs().toList(),
            itemComposable = {
                Text(it)
            },
            onItemSelected = {
                onChanged(time.input, it)
            }
        )
    }
}

@Composable
@Preview(showBackground = true, heightDp = 500)
fun InstantaneousTimeFieldEditorPreview() {
    TimeFieldEditor(
        onChanged = { _ -> },
        modifier = Modifier.padding(24.dp),
        model = TimeField.Instantaneous(
            time = Time.Valid(
                instant = Instant.now(),
                zoneOffset = null,
            ),
        )
    )
}

@Composable
@Preview(showBackground = true, heightDp = 500)
fun IntervalTimeFieldEditorPreview() {
    TimeFieldEditor(
        onChanged = { _ -> },
        modifier = Modifier.padding(24.dp),
        model = TimeField.Interval(
            start = Time.Valid(
                instant = Instant.now(),
                zoneOffset = null,
            ),
            end = Time.Valid(
                instant = Instant.now(),
                zoneOffset = null,
            ),
        )
    )
}