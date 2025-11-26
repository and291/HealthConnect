package com.example.healthconnect.components.impl.ui

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
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.TimeModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.TimeEditorComponentViewModel.Event
import java.time.Instant
import java.util.TimeZone

@Composable
internal fun TimeEditorComponent(
    model: TimeComponentModel,
    onChanged: (TimeComponentModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimeEditorComponentViewModel = viewModel(
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(TimeEditorComponentViewModel.TIME_MODEL_KEY, model)
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
            is TimeComponentModel.Instantaneous -> {
                TimeComponent(
                    labelText = "Time",
                    timeModel = model.time,
                ) { time, zone ->
                    val event = Event.OnInstantaneousTimeChanged(
                        value = time,
                        zoneId = zone
                    )
                    viewModel.onEvent(event)
                }
            }

            is TimeComponentModel.Interval -> {
                TimeComponent(
                    labelText = "Start time",
                    timeModel = model.start,
                ) { time, zone ->
                    val event = Event.OnStartTimeChanged(
                        value = time,
                        zoneId = zone
                    )
                    viewModel.onEvent(event)
                }

                TimeComponent(
                    labelText = "End time",
                    timeModel = model.end,
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
    timeModel: TimeModel,
    modifier: Modifier = Modifier,
    onChanged: (String, String?) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = timeModel.input,
            isError = timeModel is TimeModel.Invalid,
            label = { Text(labelText) },
            supportingText = {
                //TODO отобразить старое и новое значение в локали юзера, чтобы ему легче было оринетироваться в формате ISO_DATE_TIME если он с ним не знаком
                timeModel.zonedLocalizedTime?.let {
                    Text(it)
                }
            },
            onValueChange = {
                onChanged(it, timeModel.zoneOffset?.id)
            },
        )

        SelectorComponent(
            title = "Zone",
            //TODO teach selector to display errors
            supportText = "",
            selectedText = timeModel.zoneOffset?.id ?: "set",
            items = TimeZone.getAvailableIDs().toList(),
            itemComposable = {
                Text(it)
            },
            onItemSelected = {
                onChanged(timeModel.input, it)
            }
        )
    }
}

@Composable
@Preview(showBackground = true, heightDp = 500)
fun InstantaneousTimeEditorComponentPreview() {
    TimeEditorComponent(
        onChanged = { _ -> },
        modifier = Modifier.padding(24.dp),
        model = TimeComponentModel.Instantaneous(
            time = TimeModel.Valid(
                instant = Instant.now(),
                zoneOffset = null,
            ),
        )
    )
}

@Composable
@Preview(showBackground = true, heightDp = 500)
fun IntervalTimeEditorComponentPreview() {
    TimeEditorComponent(
        onChanged = { _ -> },
        modifier = Modifier.padding(24.dp),
        model = TimeComponentModel.Interval(
            start = TimeModel.Valid(
                instant = Instant.now(),
                zoneOffset = null,
            ),
            end = TimeModel.Valid(
                instant = Instant.now(),
                zoneOffset = null,
            ),
        )
    )
}