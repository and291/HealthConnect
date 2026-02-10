package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.time.Instant
import java.util.UUID

data class ExerciseRouteField(
    val time: Instant,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val horizontalAccuracy: Double?,
    val verticalAccuracy: Double?,
    override val instanceId: UUID = UUID.randomUUID()
) : Atomic(instanceId) {
    override fun isValid(): Boolean = true
}
