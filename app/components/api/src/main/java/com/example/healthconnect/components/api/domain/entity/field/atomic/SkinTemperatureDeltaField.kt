package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.util.UUID

data class SkinTemperatureDeltaField(
    val time: StringField,
    val delta: ValueField,
    override val instanceId: UUID = UUID.randomUUID(),
) : Atomic(instanceId) {
    override fun isValid(): Boolean = time.isValid() && delta.isValid()
}
