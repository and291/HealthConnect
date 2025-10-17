package com.example.healthconnect.components.api.domain.entity.metadata

import com.example.healthconnect.components.api.ui.model.DeviceModel
import java.time.Instant

data class MetadataEntity(
    val recordingMethod: Int,
    val id: String = "",
    val dataOriginPackageName: String = "",
    val lastModifiedTime: Instant = Instant.EPOCH,
    val clientRecordId: String = "",
    val clientRecordVersion: Long = 0,
    val deviceModel: DeviceModel = DeviceModel.Empty
)

