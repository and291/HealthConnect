package com.example.healthconnect.components.api.ui.model

sealed class PowerEditorModel {

    abstract val value: String

    data class Valid(
        val parsedValue: Double,
        override val value: String = parsedValue.toString(),
    ) : PowerEditorModel() {

        init {
            require(value.toDouble() == parsedValue) {
                "Different values for Valid Power model"
            }
        }
    }

    data class Invalid(
        override val value: String
    ) : PowerEditorModel()
}