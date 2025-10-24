package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.BloodGlucose
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.Temperature
import java.time.Instant
import java.time.ZoneOffset
import kotlin.reflect.KClass

class InsertRecordFactory {

    fun createDefault(recordClass: KClass<Record>): Record = when (recordClass) {

        BasalBodyTemperatureRecord::class -> BasalBodyTemperatureRecord(
            time = Instant.now(),
            zoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            temperature = Temperature.Companion.celsius(36.6)
        )

        BasalMetabolicRateRecord::class -> BasalMetabolicRateRecord(
            time = Instant.now(),
            zoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            basalMetabolicRate = Power.Companion.kilocaloriesPerDay(2500.0),
        )

        BloodGlucoseRecord::class -> BloodGlucoseRecord(
            time = Instant.now(),
            zoneOffset = ZoneOffset.UTC,
            metadata = Metadata.Companion.unknownRecordingMethod(),
            level = BloodGlucose.Companion.millimolesPerLiter(5.0)
        )

        else -> TODO()
    }
}