package com.example.healthconnect.components.api.ui.model

sealed class DeviceEditorModel {

    data object Empty : DeviceEditorModel()

    data class Specified(
        val type: Int,
        val manufacturer: String,
        val model: String,
    ) : DeviceEditorModel()
}