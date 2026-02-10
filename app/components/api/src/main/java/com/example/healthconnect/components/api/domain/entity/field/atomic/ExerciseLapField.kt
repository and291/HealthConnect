package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.time.Instant
import java.util.UUID

data class ExerciseLapField(
    val startTime: Instant,
    val endTime: Instant,
    val lengthInMeters: Double?,
    override val instanceId: UUID = UUID.randomUUID()
) : Atomic(instanceId) {
    override fun isValid(): Boolean =
        startTime.isBefore(endTime) && (lengthInMeters == null || lengthInMeters in 0.0..1000000.0)
}