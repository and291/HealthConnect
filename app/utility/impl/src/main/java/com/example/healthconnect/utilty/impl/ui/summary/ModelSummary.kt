package com.example.healthconnect.utilty.impl.ui.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.editor.api.domain.record.Steps
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun Model.Summary(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        getComponents().sortedBy { it.priority }.forEach { it.Summary() }
    }
}

@Composable
@Preview(showBackground = true)
private fun ModelSummaryPreview() {
    Steps(
        metadata = MetadataField(
            id = StringField("abc-123", StringField.Type.MetadataId(), readOnly = true),
            dataOriginPackageName = StringField("com.example.app", StringField.Type.MetadataDataOrigin(), readOnly = true),
            recordingMethod = SelectorField(Metadata.RECORDING_METHOD_MANUAL_ENTRY, SelectorField.Type.RecordingMethod()),
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
    ).Summary()
}