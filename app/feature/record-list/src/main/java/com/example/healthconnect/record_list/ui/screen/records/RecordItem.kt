package com.example.healthconnect.record_list.ui.screen.records

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthconnect.record_list.api.domain.entity.RecordModel
import com.example.healthconnect.record_list.api.ui.RecordSummaryFactory

@Composable
internal fun RecordItem(
    record: RecordModel,
    summaryFactory: RecordSummaryFactory,
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
        summaryFactory.Summary(
            record = record,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RecordItemPreview() {
    RecordItem(
        record = object : RecordModel {
            override fun metadataId(): String = "abc-123"
        },
        summaryFactory = object : RecordSummaryFactory {
            @Composable
            override fun Summary(record: RecordModel, modifier: Modifier) {
                Text(
                    text = "Steps: 8500",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier,
                )
            }
        },
        onDelete = {},
    )
}
