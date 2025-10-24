package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.model.TimeEditorInternalModel
import com.example.healthconnect.components.impl.ui.TimeEditorComponentViewModel.Event.OnTimeChanged
import java.time.Instant
import java.time.ZoneId
import java.util.TimeZone

@Composable
internal fun TimeEditorComponent(
    model: TimeEditorInternalModel,
    onTimeChanged: (TimeEditorModel) -> Unit,
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
        val timeEditorModel = when (val t = viewModel.state.timeModel) {
            is TimeEditorInternalModel.TimeModel.Invalid -> TimeEditorModel.Invalid
            is TimeEditorInternalModel.TimeModel.Valid -> TimeEditorModel.Valid(
                instant = t.instant,
                zoneOffset = viewModel.state.zoneId?.rules?.getOffset(t.instant)
            )
        }

        onTimeChanged(timeEditorModel)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        val timeModel = viewModel.state.timeModel
        val timeInputValue: String = viewModel.state.getTimeInputValue()
        val zoneIdValue: ZoneId? = viewModel.state.zoneId

        OutlinedTextField(
            value = timeInputValue,
            isError = timeModel is TimeEditorInternalModel.TimeModel.Invalid,
            label = { Text("Time") },
            supportingText = {
                //TODO отобразить старое и новое значение в локали юзера, чтобы ему легче было оринетироваться в формате ISO_DATE_TIME если он с ним не знаком
                viewModel.state.getZonedLocalizedTime()?.let {
                    Text(it)
                }
            },
            onValueChange = {
                val event = OnTimeChanged(
                    value = it,
                    zoneId = zoneIdValue?.id
                )
                viewModel.onEvent(event)
            },
        )

        SelectorComponent(
            title = "Zone",
            //TODO teach selector to display errors
            supportText = viewModel.state.setZoneAttemptErrorMessage ?: "",
            selectedText = zoneIdValue?.id ?: "set",
            items = TimeZone.getAvailableIDs().toList(),
            itemComposable = {
                Text(it)
            },
            onItemSelected = {
                val event = OnTimeChanged(
                    value = timeInputValue,
                    zoneId = it
                )
                viewModel.onEvent(event)
            }
        )
    }
}

@Composable
@Preview(showBackground = true, heightDp = 500)
fun TimeEditorComponentPreview() {

    TimeEditorComponent(
        model = TimeEditorInternalModel.create(Instant.now(), null),
        onTimeChanged = {},
        modifier = Modifier.padding(24.dp),
    )
}