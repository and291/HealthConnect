package com.example.healthconnect.components.api.domain.entity.field.atomic

import com.example.healthconnect.components.api.domain.entity.ComponentModel.Companion.PRIORITY_DEFAULT
import java.util.UUID


sealed class ValueField(override val instanceId: UUID) : Atomic(instanceId) {

    abstract val value: String
    abstract val type: Type

    data class Dbl(
        override val value: String,
        override val type: Type,
        override val instanceId: UUID = UUID.randomUUID(),
        override val priority: Int = PRIORITY_DEFAULT,
    ) : ValueField(instanceId) {

        constructor(
            parsedValue: Double,
            type: Type,
            presentationId: UUID = UUID.randomUUID(),
            priority: Int = PRIORITY_DEFAULT,
        ) : this(
            value = parsedValue.toString(),
            type = type,
            instanceId = presentationId,
            priority = priority,
        )

        val parsedValue: Double? = value.toDoubleOrNull()

        override fun isValid(): Boolean  = parsedValue != null
    }

    data class Lng(
        override val value: String,
        override val type: Type,
        override val instanceId: UUID = UUID.randomUUID(),
        override val priority: Int = PRIORITY_DEFAULT,
    ) : ValueField(instanceId) {

        constructor(
            parsedValue: Long,
            type: Type,
            presentationId: UUID = UUID.randomUUID(),
            priority: Int = PRIORITY_DEFAULT,
        ) : this(
            value = parsedValue.toString(),
            type = type,
            instanceId = presentationId,
            priority = priority,
        )

        val parsedValue: Long? = value.toLongOrNull()

        override fun isValid(): Boolean  = parsedValue != null
    }

    sealed class Type {
        abstract val label: String
        abstract val supportingText: String
        abstract val suffix: String
        open val valueType: ValueType = ValueType.Double

        enum class ValueType {
            Double, Long
        }

        data class Temperature(
            override val label: String = "Temperature",
            override val supportingText: String = "Temperature in \"Temperature\" unit.",
            override val suffix: String = "Celsius",
        ) : Type()

        data class Energy(
            override val label: String = "Energy",
            override val supportingText: String = "Energy in \"Energy\" unit. Required field. Valid range: 0-1000000 kcal.",
            override val suffix: String = "kilocalories",
        ) : Type()

        data class Power(
            override val label: String = "Power",
            override val supportingText: String = "Power in \"Power\" unit.",
            override val suffix: String = "kilocalories per day",
        ) : Type()

        data class PowerWatt(
            override val label: String = "Power",
            override val supportingText: String = "Power in Watts. Required field. Valid range: 0-100000.",
            override val suffix: String = "Watts",
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

        data class Percentage(
            override val label: String = "Percentage",
            override val supportingText: String = "Percentage. Required field. Valid range: 0-100.",
            override val suffix: String = "%",
        ) : Type()

        data class Mass(
            override val label: String = "Mass",
            override val supportingText: String = "Mass in Mass unit. Required field. Valid range: 0-1000 kilograms.",
            override val suffix: String = "kilograms",
        ) : Type()

        data class HeartRateVariabilityRmssd(
            override val label: String = "HRV",
            override val supportingText: String = "Heart rate variability in milliseconds. Required field. Valid Range: 1-200.",
            override val suffix: String = "milliseconds",
        ) : Type()

        data class Length(
            override val label: String = "Height",
            override val supportingText: String = "Height in Length unit. Required field. Valid range: 0-3 meters.",
            override val suffix: String = "meters",
        ) : Type()

        data class Distance(
            override val label: String = "Distance",
            override val supportingText: String = "Distance in Length unit. Required field. Valid range: 0-1000000 meters.",
            override val suffix: String = "meters",
        ) : Type()

        data class Elevation(
            override val label: String = "Elevation",
            override val supportingText: String = "Elevation in Length units. Required field. Valid range: -1000000-1000000 meters.",
            override val suffix: String = "meters",
        ) : Type()

        data class FloorsClimbed(
            override val label: String = "Floors climbed",
            override val supportingText: String = "Number of floors climbed. Required field. Valid range: 0-1000000.",
            override val suffix: String = "floors",
        ) : Type()

        data class VolumeOfWater(
            override val label: String = "Volume of water",
            override val supportingText: String = "Volume of water in Volume unit. Required field. Valid range: 0-100 liters.",
            override val suffix: String = "liters",
        ) : Type()

        data class RespiratoryRate(
            override val label: String = "Respiratory rate",
            override val supportingText: String = "Respiratory rate in breaths per minute. Required field. Valid range: 0-1000.",
            override val suffix: String = "breaths per minute",
        ) : Type()

        data class Vo2Max(
            override val label: String = "VO2 max",
            override val supportingText: String = "Maximal aerobic capacity (VO2 max) in milliliters. Required field. Valid range: 0-100.",
            override val suffix: String = "milliliters per minute kilogram",
        ) : Type()

        data class BeatsPerMinute(
            override val label: String = "Heart beats per minute",
            override val supportingText: String = "Heart beats per minute. Required field. Validation range: 1-300.",
            override val suffix: String = "bpm",
            override val valueType: ValueType =  ValueType.Long,
        ) : Type()

        data class PlannedExerciseBlockRepetitions(
            override val label: String = "Repetitions",
            override val supportingText: String = "Number of times the block is repeated. Required field. Valid range: 1-1000000.",
            override val suffix: String = "times",
            override val valueType: ValueType = ValueType.Long,
        ) : Type()

        data class NutritionEnergy(
            override val label: String = "Energy",
            override val supportingText: String = "Energy in \"Energy\" unit. Optional field.",
            override val suffix: String = "kilocalories",
        ) : Type()

        data class NutritionMass(
            override val label: String,
            override val supportingText: String = "$label in Mass unit. Optional field.",
            override val suffix: String = "grams",
        ) : Type()

        data class StepsCount(
            override val label: String = "Steps",
            override val supportingText: String = "Count. Required field. Valid range: 1-1000000.",
            override val suffix: String = "steps",
            override val valueType: ValueType = ValueType.Long,
        ) : Type()

        data class WheelchairPushesCount(
            override val label: String = "Wheelchair pushes",
            override val supportingText: String = "Count. Required field. Valid range: 1-1000000.",
            override val suffix: String = "pushes",
            override val valueType: ValueType = ValueType.Long,
        ) : Type()

        data class TemperatureDelta(
            override val label: String = "Temperature Delta",
            override val supportingText: String = "Temperature delta in Celsius degrees.",
            override val suffix: String = "Celsius",
        ) : Type()

        data class CyclingPedalingCadence(
            override val label: String = "Cadence",
            override val supportingText: String = "Cycling pedaling cadence in revolutions per minute. Required field. Valid range: 0-10000.",
            override val suffix: String = "rpm",
        ) : Type()

        data class Speed(
            override val label: String = "Speed",
            override val supportingText: String = "Speed in meters per second. Required field. Valid range: 0-1000000.",
            override val suffix: String = "meters per second",
        ) : Type()

        data class StepsCadence(
            override val label: String = "Steps cadence",
            override val supportingText: String = "Steps cadence in steps per minute. Required field. Valid range: 0-10000.",
            override val suffix: String = "steps per minute",
        ) : Type()
    }
}
