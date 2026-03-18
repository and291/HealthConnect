package com.example.healthconnect.utilty.impl.ui.screen.records.summary.field

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField

@Composable
@Preview(showBackground = true)
private fun SelectorFieldPreview() {
    SelectorField(
        value = Metadata.RECORDING_METHOD_MANUAL_ENTRY,
        type = SelectorField.Type.RecordingMethod(),
    ).Summary()
}

@Composable
fun SelectorField.Summary(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = "${type.title}: ",
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = map(value),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}