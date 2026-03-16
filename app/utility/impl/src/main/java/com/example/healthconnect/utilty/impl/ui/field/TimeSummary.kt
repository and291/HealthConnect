package com.example.healthconnect.utilty.impl.ui.field

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.Time
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField

@Composable
fun TimeField.Summary(modifier: Modifier = Modifier) {
    when (this) {
        is TimeField.Instantaneous -> TimeRow(
            label = "Time",
            value = (time as? Time.Valid)?.zonedLocalizedTime ?: time.input,
            modifier = modifier,
        )
        is TimeField.Interval -> {
            TimeRow(
                label = "Start",
                value = (start as? Time.Valid)?.zonedLocalizedTime ?: start.input,
                modifier = modifier,
            )
            TimeRow(
                label = "End",
                value = (end as? Time.Valid)?.zonedLocalizedTime ?: end.input,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun TimeRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}