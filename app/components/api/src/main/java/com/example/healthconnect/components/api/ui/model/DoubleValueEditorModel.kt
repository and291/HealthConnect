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

        //TODO unite with next? Keep in mind that viewmodel's key depends on this Type!
        data class SystolicPressure(
            override val label: String = "Systolic Pressure",
            override val supportingText: String = "Systolic blood pressure measurement, in Pressure unit. Required field. Valid range: 20-200mmHg. For SDK extension 17 or higher, Valid range: 20-300mmHg.",
            override val suffix: String = "millimeters of Mercury (mmHg)",
        ) : Type()

        data class DiastolicPressure(
            override val label: String = "Diastolic Pressure",
            override val supportingText: String = "Diastolic blood pressure measurement, in Pressure unit. Required field. Valid range: 10-180mmHg. For SDK extension 17 or higher, Valid range: 10-300mmHg.",
            override val suffix: String = "millimeters of Mercury (mmHg)",
        ) : Type()

        data class PercentageBodyFat(
            override val label: String = "Percentage",
            override val supportingText: String = "Percentage. Required field. Valid range: 0-100.",
            override val suffix: String = "%",
        ) : Type()
    }
}