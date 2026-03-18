package com.example.healthconnect.utilty.impl.ui.screen.records.summary.field

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.records.metadata.Device
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

@Composable
@Preview(showBackground = true)
private fun MetadataFieldPreview() {
    MetadataField(
        id = StringField("abc-123", StringField.Type.MetadataId(), readOnly = true),
        dataOriginPackageName = StringField("com.example.app", StringField.Type.MetadataDataOrigin(), readOnly = true),
        recordingMethod = SelectorField(Metadata.RECORDING_METHOD_MANUAL_ENTRY, SelectorField.Type.RecordingMethod()),
        clientRecordId = StringField("client-456", StringField.Type.MetadataClientRecordId()),
        clientRecordVersion = StringField("1", StringField.Type.MetadataClientRecordVersion()),
        lastModifiedTime = StringField("2024-01-15T09:00:00Z", StringField.Type.MetadataLastModifiedTime(), readOnly = true),
        deviceFieldComponentModel = DeviceField.Specified(Device.TYPE_WATCH, "Google", "Pixel Watch"),
    ).Summary()
}

@Composable
fun MetadataField.Summary(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        id.Summary(modifier)
        dataOriginPackageName.Summary(modifier)
        recordingMethod.Summary(modifier)
        clientRecordId.Summary(modifier)
        clientRecordVersion.Summary(modifier)
        lastModifiedTime.Summary(modifier)
        deviceFieldComponentModel.Summary(modifier)
    }
}