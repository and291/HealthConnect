package com.example.healthconnect.utilty.impl.ui.field

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField

@Composable
fun ValueField.Summary(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = "${type.label}: ",
            style = MaterialTheme.typography.labelSmall,
        )
        Text(
            text = "$value ${type.suffix}",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}