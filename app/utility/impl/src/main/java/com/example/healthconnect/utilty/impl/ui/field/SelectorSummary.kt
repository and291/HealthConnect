package com.example.healthconnect.utilty.impl.ui.field

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField

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