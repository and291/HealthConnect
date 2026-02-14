package com.example.healthconnect.editor.impl.ui.editor

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.ActivityIntensityRecord
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.PlannedExerciseSessionRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.WeightRecord
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.editor.impl.ui.editor.record.ActiveCaloriesBurnedEditor
import com.example.healthconnect.editor.impl.ui.editor.record.ActivityIntensityEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BasalBodyTemperatureEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BasalMetabolicRateEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BloodGlucoseEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BloodPressureEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BodyFatEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BodyTemperatureEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BodyWaterMassEditor
import com.example.healthconnect.editor.impl.ui.editor.record.BoneMassEditor
import com.example.healthconnect.editor.impl.ui.editor.record.CervicalMucusEditor
import com.example.healthconnect.editor.impl.ui.editor.record.DistanceEditor
import com.example.healthconnect.editor.impl.ui.editor.record.Editor
import com.example.healthconnect.editor.impl.ui.editor.record.ElevationGainedEditor
import com.example.healthconnect.editor.impl.ui.editor.record.ExerciseSessionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.FloorsClimbedEditor
import com.example.healthconnect.editor.impl.ui.editor.record.HeartRateVariabilityRmssdEditor
import com.example.healthconnect.editor.impl.ui.editor.record.HeightEditor
import com.example.healthconnect.editor.impl.ui.editor.record.HydrationEditor
import com.example.healthconnect.editor.impl.ui.editor.record.IntermenstrualBleedingEditor
import com.example.healthconnect.editor.impl.ui.editor.record.LeanBodyMassEditor
import com.example.healthconnect.editor.impl.ui.editor.record.MenstruationFlowEditor
import com.example.healthconnect.editor.impl.ui.editor.record.MenstruationPeriodEditor
import com.example.healthconnect.editor.impl.ui.editor.record.MindfulnessSessionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.OvulationTestEditor
import com.example.healthconnect.editor.impl.ui.editor.record.OxygenSaturationEditor
import com.example.healthconnect.editor.impl.ui.editor.record.PlannedExerciseSessionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.RespiratoryRateEditor
import com.example.healthconnect.editor.impl.ui.editor.record.RestingHeartRateEditor
import com.example.healthconnect.editor.impl.ui.editor.record.SexualActivityEditor
import com.example.healthconnect.editor.impl.ui.editor.record.Vo2MaxEditor
import com.example.healthconnect.editor.impl.ui.editor.record.WeightEditor
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
        CervicalMucusRecord::class -> CervicalMucusEditor()
        HeartRateVariabilityRmssdRecord::class -> HeartRateVariabilityRmssdEditor()
        HeightRecord::class -> HeightEditor()
        IntermenstrualBleedingRecord::class -> IntermenstrualBleedingEditor()
        LeanBodyMassRecord::class -> LeanBodyMassEditor()
        WeightRecord::class -> WeightEditor()
        OxygenSaturationRecord::class -> OxygenSaturationEditor()
        RespiratoryRateRecord::class -> RespiratoryRateEditor()
        MenstruationFlowRecord::class -> MenstruationFlowEditor()
        OvulationTestRecord::class -> OvulationTestEditor()
        SexualActivityRecord::class -> SexualActivityEditor()
        Vo2MaxRecord::class -> Vo2MaxEditor()
        RestingHeartRateRecord::class -> RestingHeartRateEditor()
        ActiveCaloriesBurnedRecord::class -> ActiveCaloriesBurnedEditor()
        DistanceRecord::class -> DistanceEditor()
        ElevationGainedRecord::class -> ElevationGainedEditor()
        HydrationRecord::class -> HydrationEditor()
        ActivityIntensityRecord::class -> ActivityIntensityEditor()
        FloorsClimbedRecord::class -> FloorsClimbedEditor()
        MenstruationPeriodRecord::class -> MenstruationPeriodEditor()
        MindfulnessSessionRecord::class -> MindfulnessSessionEditor()
        ExerciseSessionRecord::class -> ExerciseSessionEditor()
        PlannedExerciseSessionRecord::class -> PlannedExerciseSessionEditor()
        else -> throw NotImplementedError()
    } as Editor<Record, Model>
}