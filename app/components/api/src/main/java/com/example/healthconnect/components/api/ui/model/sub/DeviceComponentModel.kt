package com.example.healthconnect.components.api.ui.model.sub

import com.example.healthconnect.components.api.ui.model.ComponentModel

sealed class DeviceComponentModel : ComponentModel {

    data object Empty : DeviceComponentModel() {
        override fun isValid(): Boolean = true
    }

    data class Specified(
        val type: Int,
        val manufacturer: String,
        val model: String,
    ) : DeviceComponentModel() {
        override fun isValid(): Boolean = true
    }
}