package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.time.Instant
import java.util.UUID

data class ExerciseSegmentField(
    val startTime: Instant,
    val endTime: Instant,
    val segmentType: Int,
    val repetitions: Int,
    override val instanceId: UUID = UUID.randomUUID()
) : Atomic(instanceId) {
    override fun isValid(): Boolean = startTime.isBefore(endTime) && repetitions >= 0
}