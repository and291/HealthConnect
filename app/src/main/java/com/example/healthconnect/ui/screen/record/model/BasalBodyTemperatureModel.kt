package com.example.healthconnect.ui.screen.record.model

import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocations
import androidx.health.connect.client.units.Temperature
import com.example.healthconnect.domain.entity.metadata.MetadataEntity
import java.time.Instant
import java.time.ZoneOffset

data class BasalBodyTemperatureModel(
    val time: Instant,
    val zoneOffset: ZoneOffset?,
    override val metadataEntity: MetadataEntity,
    val temperature: Temperature,
    @property:BodyTemperatureMeasurementLocations
    val measurementLocation: Int = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
) : RecordModel