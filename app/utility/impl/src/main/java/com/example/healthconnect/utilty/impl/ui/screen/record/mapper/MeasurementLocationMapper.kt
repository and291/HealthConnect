package com.example.healthconnect.utilty.impl.ui.screen.record.mapper

import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation

class MeasurementLocationMapper {

    val locations = listOf(
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
    )

    fun map(location: Int): String {
        val item = locations.find { x -> x.first == location }
        return requireNotNull(item?.second) { "Location = $location not found among available measurement locations" }
    }
}