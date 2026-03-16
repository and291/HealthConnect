package com.example.healthconnect.utilty.impl.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthconnect.utilty.impl.ui.field.Summary
import com.example.healthconnect.utilty.impl.ui.model.DisplayRecord

@Composable
fun RecordItem(
    record: DisplayRecord,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Button(onClick = onDelete) {
            Text("X")
        }
        val contentModifier = Modifier.padding(start = 8.dp)
        record.model.Summary(contentModifier)
    }
}