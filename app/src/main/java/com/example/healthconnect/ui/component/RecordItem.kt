package com.example.healthconnect.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RecordItem(
    text: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Button(onClick = onDelete) {
            Text("X")
        }
        Text(text)
    }
}

@Composable
@Preview(widthDp = 100, heightDp = 300, showBackground = true)
fun RecordItemPreview() {
    RecordItem(
        text = "my text is here",
        onDelete = {},
    )
}
