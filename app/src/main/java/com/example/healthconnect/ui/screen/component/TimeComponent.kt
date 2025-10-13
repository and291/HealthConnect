package com.example.healthconnect.ui.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.component.TimeComponentViewModel.Companion.TIME_MODEL_KEY
import com.example.healthconnect.ui.screen.component.TimeComponentViewModel.Event.OnTimeChanged
import com.example.healthconnect.ui.screen.component.model.TimeComponentModel
import com.example.healthconnect.ui.screen.component.model.TimeComponentModel.TimeModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.TimeZone

@Composable
fun TimeComponent(
    instant: Instant,
    modifier: Modifier = Modifier,
    zoneOffset: ZoneOffset? = null,
): Unit = TimeComponent(
    modifier = modifier,
    viewModel = viewModel(
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(TIME_MODEL_KEY, TimeComponentModel.create(instant, zoneOffset))
        }
    )
)

@Composable
fun TimeComponent(
    viewModel: TimeComponentViewModel,
    modifier: Modifier = Modifier
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        val timeModel = viewModel.state.timeModel
        val timeInputValue: String = viewModel.state.getTimeInputValue()
        val zoneIdValue: ZoneId? = viewModel.state.zoneId

        OutlinedTextField(
            value = timeInputValue,
            isError = timeModel is TimeModel.Invalid,
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
fun TimeComponentPreview() {

    TimeComponent(
        instant = Instant.now(),
        modifier = Modifier.padding(24.dp),
        zoneOffset = null,
    )
}