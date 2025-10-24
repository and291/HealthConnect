package com.example.healthconnect.components.api.ui.model

sealed class DoubleValueEditorModel : ComponentEditorModel() {

    abstract val value: String
    abstract val type: Type

    data class Valid(
        val parsedValue: Double,
        override val value: String = parsedValue.toString(),
        override val type: Type,
    ) : DoubleValueEditorModel() {

        init {
            require(value.toDouble() == parsedValue) {
                "Different values for Valid ${type.label} model"
            }
        }
    }

    data class Invalid(
        override val value: String,
        override val type: Type,
    ) : DoubleValueEditorModel()

    sealed class Type {
        abstract val label: String
        abstract val supportingText: String
        abstract val suffix: String

        data class Temperature(
            override val label: String = "Temperature",
            override val supportingText: String = "Temperature in \"Temperature\" unit.",
            override val suffix: String = "Celsius",
        ) : Type()

        data class Power(
            override val label: String = "Power",
            override val supportingText: String = "Power in \"Power\" unit.",
            override val suffix: String = "kilocalories per day",
        ) : Type()

        data class BloodGlucoseLevel(
            override val label: String = "Blood glucose",
            override val supportingText: String = "Blood glucose level or concentration. Required field. Valid range: 0-50 mmol/L.",
            override val suffix: String = "millimoles per liter",
        ) : Type()
    }
}