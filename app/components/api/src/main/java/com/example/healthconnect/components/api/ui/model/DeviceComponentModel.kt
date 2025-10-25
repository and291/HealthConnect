package com.example.healthconnect.components.api.ui.model

sealed class DeviceComponentModel {

    data object Empty : DeviceComponentModel()

    data class Specified(
        val type: Int,
        val manufacturer: String,
        val model: String,
    ) : DeviceComponentModel()
}