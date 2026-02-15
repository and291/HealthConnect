package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.util.UUID

data class SleepSessionStageField(
    val startTime: StringField,
    val endTime: StringField,
    val stage: Int,
    override val instanceId: UUID = UUID.randomUUID(),
) : Atomic(instanceId) {
    override fun isValid(): Boolean = startTime.isValid() && endTime.isValid()
}
