package com.example.healthconnect.components.api.ui.model

sealed class BloodGlucoseLevelEditorModel : ComponentEditorModel() {

    abstract val value: String

    data class Valid(
        val parsedValue: Double,
        override val value: String = parsedValue.toString(),
    ) : BloodGlucoseLevelEditorModel() {

        init {
            require(value.toDouble() == parsedValue) {
                "Different values for Valid BloodGlucoseLevel model"
            }
        }
    }

    data class Invalid(
        override val value: String
    ) : BloodGlucoseLevelEditorModel()
}