package com.example.healthconnect.components.api.ui.model

import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_AFTER_MEAL
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_BEFORE_MEAL
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_FASTING
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_GENERAL
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_UNKNOWN
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BloodPressureRecord.Companion.BODY_POSITION_LYING_DOWN
import androidx.health.connect.client.records.BloodPressureRecord.Companion.BODY_POSITION_RECLINING
import androidx.health.connect.client.records.BloodPressureRecord.Companion.BODY_POSITION_SITTING_DOWN
import androidx.health.connect.client.records.BloodPressureRecord.Companion.BODY_POSITION_STANDING_UP
import androidx.health.connect.client.records.BloodPressureRecord.Companion.BODY_POSITION_UNKNOWN
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.MealType.MEAL_TYPE_BREAKFAST
import androidx.health.connect.client.records.MealType.MEAL_TYPE_DINNER
import androidx.health.connect.client.records.MealType.MEAL_TYPE_LUNCH
import androidx.health.connect.client.records.MealType.MEAL_TYPE_SNACK
import androidx.health.connect.client.records.MealType.MEAL_TYPE_UNKNOWN
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.Vo2MaxRecord

data class SelectorComponentModel(
    val value: Int,
    val type: Type,
) : ComponentModel() {

    override fun isValid(): Boolean  = type.items.any { it.first == value }

    fun map(item: Int): String {
        val foundItem = type.items.find { it.first == item }
        return requireNotNull(foundItem?.second) {
            "${type.title} = $item not found among available items: ${type.items}"
        }
    }

    sealed class Type {
        abstract val title: String
        abstract val supportText: String
        abstract val items: List<Pair<Int, String>>

        data class MeasurementLocationBodyTemperature(
            override val title: String = "Measurement Location",
            override val supportText: String = "Where on the user's body the temperature measurement was taken from. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN to "UNKNOWN",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_ARMPIT to "ARMPIT",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_FINGER to "FINGER",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_FOREHEAD to "FOREHEAD",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_MOUTH to "MOUTH",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_RECTUM to "RECTUM",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_TEMPORAL_ARTERY to "TEMPORAL_ARTERY",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_TOE to "TOE",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_EAR to "EAR",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_WRIST to "WRIST",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_VAGINA to "VAGINA",
            ),
        ) : Type()

        data class SpecimenSource(
            override val title: String = "Specimen Source",
            override val supportText: String = "Type of body fluid used to measure the blood glucose. Optional, enum field.",
            override val items: List<Pair<Int, String>> = listOf(
                BloodGlucoseRecord.SPECIMEN_SOURCE_UNKNOWN to "UNKNOWN",
                BloodGlucoseRecord.SPECIMEN_SOURCE_INTERSTITIAL_FLUID to "INTERSTITIAL_FLUID",
                BloodGlucoseRecord.SPECIMEN_SOURCE_CAPILLARY_BLOOD to "CAPILLARY_BLOOD",
                BloodGlucoseRecord.SPECIMEN_SOURCE_PLASMA to "PLASMA",
                BloodGlucoseRecord.SPECIMEN_SOURCE_SERUM to "SERUM",
                BloodGlucoseRecord.SPECIMEN_SOURCE_TEARS to "TEARS",
                BloodGlucoseRecord.SPECIMEN_SOURCE_WHOLE_BLOOD to "WHOLE_BLOOD",
            ),
        ) : Type()

        data class MealType(
            override val title: String = "Meal Type",
            override val supportText: String = "Type of meal related to the blood glucose measurement. Optional, enum field.",
            override val items: List<Pair<Int, String>> = listOf(
                MEAL_TYPE_UNKNOWN to "UNKNOWN",
                MEAL_TYPE_BREAKFAST to "BREAKFAST",
                MEAL_TYPE_LUNCH to "LUNCH",
                MEAL_TYPE_DINNER to "DINNER",
                MEAL_TYPE_SNACK to "SNACK",
            ),
        ) : Type()

        data class RelationToMeal(
            override val title: String = "Relation To Meal",
            override val supportText: String = "Relationship of the meal to the blood glucose measurement. Optional, enum field.",
            override val items: List<Pair<Int, String>> = listOf(
                RELATION_TO_MEAL_UNKNOWN to "UNKNOWN",
                RELATION_TO_MEAL_GENERAL to "GENERAL",
                RELATION_TO_MEAL_FASTING to "FASTING",
                RELATION_TO_MEAL_BEFORE_MEAL to "BEFORE_MEAL",
                RELATION_TO_MEAL_AFTER_MEAL to "AFTER_MEAL",
            ),
        ) : Type()

        data class BodyPosition(
            override val title: String = "Body Position",
            override val supportText: String = "The user's body position when the measurement was taken. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                BODY_POSITION_UNKNOWN to "UNKNOWN",
                BODY_POSITION_STANDING_UP to "STANDING_UP",
                BODY_POSITION_SITTING_DOWN to "SITTING_DOWN",
                BODY_POSITION_LYING_DOWN to "LYING_DOWN",
                BODY_POSITION_RECLINING to "RECLINING",
            ),
        ) : Type()

        data class MeasurementLocationBloodPressure(
            override val title: String = "Measurement Location",
            override val supportText: String = "The arm and part of the arm where the measurement was taken. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                BloodPressureRecord.MEASUREMENT_LOCATION_UNKNOWN to "UNKNOWN",
                BloodPressureRecord.MEASUREMENT_LOCATION_LEFT_WRIST to "LEFT_WRIST",
                BloodPressureRecord.MEASUREMENT_LOCATION_RIGHT_WRIST to "RIGHT_WRIST",
                BloodPressureRecord.MEASUREMENT_LOCATION_LEFT_UPPER_ARM to "LEFT_UPPER_ARM",
                BloodPressureRecord.MEASUREMENT_LOCATION_RIGHT_UPPER_ARM to "RIGHT_UPPER_ARM",
            ),
        ) : Type()

        data class Appearance(
            override val title: String = "Appearance",
            override val supportText: String = "The consistency of the user's cervical mucus.",
            override val items: List<Pair<Int, String>> = listOf(
                CervicalMucusRecord.APPEARANCE_UNKNOWN to "UNKNOWN",
                CervicalMucusRecord.APPEARANCE_DRY to "DRY",
                CervicalMucusRecord.APPEARANCE_STICKY to "STICKY",
                CervicalMucusRecord.APPEARANCE_CREAMY to "CREAMY",
                CervicalMucusRecord.APPEARANCE_WATERY to "WATERY",
                CervicalMucusRecord.APPEARANCE_EGG_WHITE to "EGG_WHITE",
                CervicalMucusRecord.APPEARANCE_UNUSUAL to "UNUSUAL",
            ),
        ) : Type()

        data class Sensation(
            override val title: String = "Sensation",
            override val supportText: String = "The feel of the user's cervical mucus.",
            override val items: List<Pair<Int, String>> = listOf(
                CervicalMucusRecord.SENSATION_UNKNOWN to "UNKNOWN",
                CervicalMucusRecord.SENSATION_LIGHT to "LIGHT",
                CervicalMucusRecord.SENSATION_MEDIUM to "MEDIUM",
                CervicalMucusRecord.SENSATION_HEAVY to "HEAVY",
            ),
        ) : Type()

        data class Flow(
            override val title: String = "Flow",
            override val supportText: String = "How heavy the user's menstrual flow was. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                MenstruationFlowRecord.FLOW_UNKNOWN to "UNKNOWN",
                MenstruationFlowRecord.FLOW_LIGHT to "LIGHT",
                MenstruationFlowRecord.FLOW_MEDIUM to "MEDIUM",
                MenstruationFlowRecord.FLOW_HEAVY to "HEAVY",
            ),
        ) : Type()

        data class Result(
            override val title: String = "Result of an ovulation test",
            override val supportText: String = "The result of a user's ovulation test, which shows if they're ovulating or not. Required field.",
            //TODO add extended description (from javadoc)
            override val items: List<Pair<Int, String>> = listOf(
                OvulationTestRecord.RESULT_POSITIVE to "POSITIVE",
                OvulationTestRecord.RESULT_HIGH to "HIGH",
                OvulationTestRecord.RESULT_NEGATIVE to "NEGATIVE",
                OvulationTestRecord.RESULT_INCONCLUSIVE to "INCONCLUSIVE"
            ),
        ) : Type()

        data class ProtectionUsed(
            override val title: String = "Protection",
            override val supportText: String = "Whether protection was used during sexual activity. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                SexualActivityRecord.PROTECTION_USED_UNKNOWN to "UNKNOWN",
                SexualActivityRecord.PROTECTION_USED_PROTECTED to "PROTECTED",
                SexualActivityRecord.PROTECTION_USED_UNPROTECTED to "UNPROTECTED",
            ),
        ) : Type()

        data class Vo2MaxMeasurementMethod(
            override val title: String = "Measurement method",
            override val supportText: String = "VO2 max measurement method. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                Vo2MaxRecord.MEASUREMENT_METHOD_OTHER to "OTHER",
                Vo2MaxRecord.MEASUREMENT_METHOD_METABOLIC_CART to "METABOLIC CART",
                Vo2MaxRecord.MEASUREMENT_METHOD_HEART_RATE_RATIO to "HEART RATE RATIO",
                Vo2MaxRecord.MEASUREMENT_METHOD_COOPER_TEST to "COOPER TEST",
                Vo2MaxRecord.MEASUREMENT_METHOD_MULTISTAGE_FITNESS_TEST to "MULTISTAGE FITNESS TEST",
                Vo2MaxRecord.MEASUREMENT_METHOD_ROCKPORT_FITNESS_TEST to "ROCKPORT FITNESS TEST",
            ),
        ) : Type()
    }
}