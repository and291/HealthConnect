package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.time.Instant
import java.util.UUID

data class SpeedSampleField(
    val time: Instant,
    val speed: ValueField,
    override val instanceId: UUID = UUID.randomUUID()
) : Atomic(instanceId) {
    override fun isValid(): Boolean = speed.isValid()
}
