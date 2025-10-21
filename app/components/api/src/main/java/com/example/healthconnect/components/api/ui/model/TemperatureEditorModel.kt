package com.example.healthconnect.components.api.ui.model

sealed class TemperatureEditorModel {

    abstract val value: String

    data class Valid(
        val parsedValue: Double,
        override val value: String = parsedValue.toString(),
    ) : TemperatureEditorModel() {

        init {
            require(value.toDouble() == parsedValue) {
                "Different values for Valid Temperature model"
            }
        }
    }

    data class Invalid(
        override val value: String
    ) : TemperatureEditorModel()
}