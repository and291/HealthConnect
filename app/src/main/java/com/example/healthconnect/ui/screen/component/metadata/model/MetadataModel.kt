package com.example.healthconnect.ui.screen.component.metadata.model

import java.time.Instant

data class MetadataModel(
    val recordingMethod: Int,
    val id: String = "",
    val dataOriginPackageName: String = "",
    val lastModifiedTime: Instant = Instant.EPOCH,
    val clientRecordId: String = "",
    val clientRecordVersion: Long = 0,
    val deviceModel: DeviceModel = DeviceModel.Empty
)

