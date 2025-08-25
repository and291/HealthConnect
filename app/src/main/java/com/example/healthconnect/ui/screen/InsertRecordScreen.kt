package com.example.healthconnect.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import kotlin.reflect.KClass

@Composable
fun InsertRecordScreen(
    recordType: KClass<out Record>,
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
    InsertRecordScreen(
        recordType = StepsRecord::class
    )
}
