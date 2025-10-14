package com.example.healthconnect.utilty.impl.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InsertRecordScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text("Metadata:")
        TextField(
            value = "",
            onValueChange = {}
        )
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 720, showBackground = true)
fun InsertRecordScreenPreview() {
    InsertRecordScreen()
}
