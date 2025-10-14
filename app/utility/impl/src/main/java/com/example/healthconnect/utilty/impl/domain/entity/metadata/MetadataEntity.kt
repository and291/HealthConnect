package com.example.healthconnect.utilty.impl.domain.entity.metadata

import java.time.Instant

data class MetadataEntity(
    val recordingMethod: Int,
    val id: String = "",
    val dataOriginPackageName: String = "",
    val lastModifiedTime: Instant = Instant.EPOCH,
    val clientRecordId: String = "",
    val clientRecordVersion: Long = 0,
    val deviceEntity: DeviceEntity = DeviceEntity.Empty
)

