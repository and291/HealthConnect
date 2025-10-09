package com.example.healthconnect.ui.screen.component.metadata.model

sealed class DeviceModel {

    data object Empty : DeviceModel()

    data class Specified(
        val type: Int,
        val manufacturer: String,
        val model: String,
    ) : DeviceModel()
}