package com.example.healthconnect.components.api.ui.model

import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation

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
}