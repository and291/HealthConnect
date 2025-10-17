package com.example.healthconnect.components.api.ui.model

sealed class TemperatureEditorModel {

    abstract val value: String

    data class Valid(
        val temperatureCelsius: Double
    ) : TemperatureEditorModel() {

        override val value: String
            get() = temperatureCelsius.toString()
    }

    data class Invalid(
        override val value: String
    ) : TemperatureEditorModel()
}