package com.example.healthconnect.utilty.impl.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Metadata as HCMetadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.editor.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.ui.model.DisplayRecord
import com.example.healthconnect.utilty.impl.ui.summary.Summary
import java.time.Instant
import java.time.ZoneOffset

@Composable
@Preview(showBackground = true)
private fun RecordItemPreview() {
    RecordItem(
        record = DisplayRecord(
            model = Steps(
                metadata = MetadataField(
                    id = StringField("abc-123", StringField.Type.MetadataId(), readOnly = true),
                    dataOriginPackageName = StringField("com.example.app", StringField.Type.MetadataDataOrigin(), readOnly = true),
                    recordingMethod = SelectorField(HCMetadata.RECORDING_METHOD_MANUAL_ENTRY, SelectorField.Type.RecordingMethod()),
                    clientRecordId = StringField("", StringField.Type.MetadataClientRecordId()),
                    clientRecordVersion = StringField("1", StringField.Type.MetadataClientRecordVersion()),
                    lastModifiedTime = StringField("2024-01-15T09:00:00Z", StringField.Type.MetadataLastModifiedTime(), readOnly = true),
                ),
                time = TimeField.Interval(
                    startTime = Instant.parse("2024-01-15T09:00:00Z"),
                    startZoneOffset = ZoneOffset.UTC,
                    endTime = Instant.parse("2024-01-15T10:00:00Z"),
                    endZoneOffset = ZoneOffset.UTC,
                ),
                count = ValueField.Lng(parsedValue = 8500L, type = ValueField.Type.StepsCount()),
            ),
            record = StepsRecord(
                startTime = Instant.parse("2024-01-15T09:00:00Z"),
                startZoneOffset = ZoneOffset.UTC,
                endTime = Instant.parse("2024-01-15T10:00:00Z"),
                endZoneOffset = ZoneOffset.UTC,
                count = 8500L,
                metadata = HCMetadata.unknownRecordingMethod(),
            ),
        ),
        onDelete = {},
    )
}

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