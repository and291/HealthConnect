package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.time.Instant
import java.util.UUID

data class StepsCadenceSampleField(
    val time: Instant,
    val cadence: ValueField,
    override val instanceId: UUID = UUID.randomUUID()
) : Atomic(instanceId) {
    override fun isValid(): Boolean = cadence.isValid()
}
