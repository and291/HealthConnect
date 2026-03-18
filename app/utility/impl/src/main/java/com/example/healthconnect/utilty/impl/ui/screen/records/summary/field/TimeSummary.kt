package com.example.healthconnect.utilty.impl.ui.screen.records.summary.field

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthconnect.components.api.domain.entity.Time
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun TimeField.Summary(modifier: Modifier = Modifier) {
    when (this) {
        is TimeField.Instantaneous -> TimeRow(
            label = "Time",
            value = (time as? Time.Valid)?.zonedLocalizedTime ?: time.input,
            modifier = modifier,
        )
        is TimeField.Interval -> Column(modifier = modifier){
            TimeRow(
                label = "Start",
                value = (start as? Time.Valid)?.zonedLocalizedTime ?: start.input,
            )
            TimeRow(
                label = "End",
                value = (end as? Time.Valid)?.zonedLocalizedTime ?: end.input,
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

@Composable
@Preview(showBackground = true)
private fun TimeFieldInstantaneousPreview() {
    TimeField.Instantaneous(
        instant = Instant.parse("2024-01-15T09:00:00Z"),
        zoneOffset = ZoneOffset.UTC,
    ).Summary()
}

@Composable
@Preview(showBackground = true)
private fun TimeFieldIntervalPreview() {
    TimeField.Interval(
        startTime = Instant.parse("2024-01-15T09:00:00Z"),
        startZoneOffset = ZoneOffset.UTC,
        endTime = Instant.parse("2024-01-15T10:00:00Z"),
        endZoneOffset = ZoneOffset.UTC,
    ).Summary()
}