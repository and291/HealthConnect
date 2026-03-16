package com.example.healthconnect.utilty.impl.ui.field

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField

@Composable
fun MetadataField.Summary(modifier: Modifier = Modifier) {
    id.Summary(modifier)
    dataOriginPackageName.Summary(modifier)
    recordingMethod.Summary(modifier)
    clientRecordId.Summary(modifier)
    clientRecordVersion.Summary(modifier)
    lastModifiedTime.Summary(modifier)
}