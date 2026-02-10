package com.example.healthconnect.components.impl.ui.editor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
internal fun TimeEditor(
    labelText: String,
    instant: Instant,
    onChanged: (Instant) -> Unit,
) {
    val input = instant.toString()
    val localizedTime = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.LONG)
        .withLocale(Locale.getDefault())
        .format(instant.atZone(ZoneId.systemDefault()))

    OutlinedTextField(
        value = input,
        onValueChange = { newValue ->
            try {
                onChanged(Instant.parse(newValue))
            } catch (_: Exception) {
                // Ignore invalid input for now
            }
        },
        label = { Text(labelText) },
        supportingText = { Text(localizedTime) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeEditorPreview() {
    TimeEditor(
        labelText = "Start Time",
        instant = Instant.now(),
        onChanged = {}
    )
}
