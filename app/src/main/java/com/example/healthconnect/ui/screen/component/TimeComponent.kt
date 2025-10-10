package com.example.healthconnect.ui.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.component.TimeComponentViewModel.Event.OnTimeValueChanged
import com.example.healthconnect.ui.screen.component.TimeComponentViewModel.Event.OnZoneSelected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.TimeZone

@Composable
fun TimeComponent(
    time: Instant,
    modifier: Modifier = Modifier,
    zoneOffset: ZoneOffset? = null,
    viewModel: TimeComponentViewModel = viewModel(
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(TimeComponentViewModel.INSTANT_KEY, time)
            set(TimeComponentViewModel.ZONE_OFFSET_KEY, zoneOffset)
        }
    )
) {

    //Добавить сюда вьюмодель которая будет валидировать инпут даты и обрабатывать выбор таймзоны

    Row(
        modifier = modifier
    ) {
        Column {
            Text(text = "time")
            // Использую формат ISO, потому что среди всех форматов, которые я успел попробовать,
            // он оказался самым удобным для редактирования через клавиатуру —
            // отображает все компоненты даты и времени в одном месте.
            TextField(
                value = LocalDateTime
                    .ofInstant(viewModel.state.first, viewModel.state.second ?: ZoneOffset.systemDefault())
                    .format(DateTimeFormatter.ISO_DATE_TIME),
                onValueChange = {
                    viewModel.onEvent(OnTimeValueChanged(it))
                }
            )

            //TODO отобразить старое и новое значение в локали юзера, чтобы ему легче было оринетироваться в формате ISO_DATE_TIME если он с ним не знаком
            val localizedDateTimeFormatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.LONG)
                .withLocale(Locale.getDefault())
            val zonedDateTime = viewModel.state.first.atZone(viewModel.state.second ?: ZoneOffset.systemDefault())
            Text(text = zonedDateTime.format(localizedDateTimeFormatter))
        }

        SelectorComponent(
            title = "timezone",
            supportText = "ffff",
            selectedText = viewModel.state.second?.toString() ?: "not set",
            items = TimeZone.getAvailableIDs().toList(),
            itemComposable = {
                Text(it)
            },
            onItemSelected = {
                viewModel.onEvent(OnZoneSelected(it))
            }
        )
    }
}

@Composable
@Preview(showBackground = true, heightDp = 500)
fun TimeComponentPreview() {

    TimeComponent(
        time = Instant.now(),
        modifier = Modifier.padding(24.dp)
    )
}