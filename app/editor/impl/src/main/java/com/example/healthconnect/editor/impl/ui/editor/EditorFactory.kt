package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.Record
import com.example.healthconnect.editor.api.ui.model.Model
import kotlin.reflect.KClass

class EditorFactory {

    @Suppress("UNCHECKED_CAST")
    fun create(
        recordKClass: KClass<out Record>,
    ): Editor<Record, Model> = when (recordKClass) {
        BasalBodyTemperatureRecord::class -> BasalBodyTemperatureEditor()
        BodyTemperatureRecord::class -> BodyTemperatureEditor()
        BasalMetabolicRateRecord::class -> BasalMetabolicRateEditor()
        BloodGlucoseRecord::class -> BloodGlucoseEditor()
        BloodPressureRecord::class -> BloodPressureEditor()
        BodyFatRecord::class -> BodyFatEditor()
        BodyWaterMassRecord::class -> BodyWaterMassEditor()
        BoneMassRecord::class -> BoneMassEditor()
        else -> throw NotImplementedError()
    } as Editor<Record, Model>
}