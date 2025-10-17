package com.example.healthconnect.components.api.ui.model

sealed class DeviceModel {

    data object Empty : DeviceModel()

    data class Specified(
        val type: Int,
        val manufacturer: String,
        val model: String,
    ) : DeviceModel()
}