package com.example.healthconnect.components.api.ui.model

sealed class PowerEditorModel {

    abstract val value: String

    data class Valid(
        val kilocaloriesPerDay: Double
    ) : PowerEditorModel() {

        override val value: String
            get() = kilocaloriesPerDay.toString()
    }

    data class Invalid(
        override val value: String
    ) : PowerEditorModel()
}