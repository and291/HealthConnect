package com.example.healthconnect.components.api.domain.entity.metadata

sealed class DeviceEntity {

    data object Empty : DeviceEntity()

    data class Specified(
        val type: Int,
        val manufacturer: String,
        val model: String,
    ) : DeviceEntity()
}