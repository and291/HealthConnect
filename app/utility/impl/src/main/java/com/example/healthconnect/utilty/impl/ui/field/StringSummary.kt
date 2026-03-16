package com.example.healthconnect.utilty.impl.ui.field

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField

@Composable
fun StringField.Summary(modifier: Modifier = Modifier) {
    if (value.isBlank()) return
    Row(modifier = modifier) {
        Text(
            text = "${type.label}: ",
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}