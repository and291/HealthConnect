package com.example.healthconnect.components.api.ui.model

import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_AFTER_MEAL
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_BEFORE_MEAL
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_FASTING
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_GENERAL
import androidx.health.connect.client.records.BloodGlucoseRecord.Companion.RELATION_TO_MEAL_UNKNOWN
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.MealType.MEAL_TYPE_BREAKFAST
import androidx.health.connect.client.records.MealType.MEAL_TYPE_DINNER
import androidx.health.connect.client.records.MealType.MEAL_TYPE_LUNCH
import androidx.health.connect.client.records.MealType.MEAL_TYPE_SNACK
import androidx.health.connect.client.records.MealType.MEAL_TYPE_UNKNOWN

sealed class SelectorType {
    abstract val title: String
    abstract val supportText: String
    abstract val items: List<Pair<Int, String>>

    fun map(item: Int): String {
        val foundItem = items.find { x -> x.first == item }
        return requireNotNull(foundItem?.second) { "$title = $item not found among available items: $items" }
    }

    data class BodyTemperatureMeasurementLocationType(
        override val title: String = "Measurement Location",
        override val supportText: String = "Where on the user's basal body the temperature measurement was taken from. Optional field.",
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
    ) : SelectorType()

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
    ) : SelectorType()

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
    ) : SelectorType()

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
    ) : SelectorType()
}