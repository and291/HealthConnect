package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.util.UUID


sealed class DeviceField(override val instanceId: UUID) : Atomic(instanceId) {

    data class Empty(
        override val instanceId: UUID = UUID.randomUUID()
    ) : DeviceField(instanceId) {

        override fun isValid(): Boolean = true
    }

    data class Specified(
        val type: Int,
        val manufacturer: String,
        val model: String,
        override val instanceId: UUID = UUID.randomUUID()
    ) : DeviceField(instanceId) {
        override fun isValid(): Boolean = true
    }
}