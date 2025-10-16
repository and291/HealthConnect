package com.example.healthconnect.components.api.ui.model

sealed class TemperatureModel {

    abstract val value: String

    data class Valid(
        val temperatureCelsius: Double
    ) : TemperatureModel() {

        override val value: String
            get() = temperatureCelsius.toString()
    }

    data class Invalid(
        override val value: String
    ) : TemperatureModel()
}