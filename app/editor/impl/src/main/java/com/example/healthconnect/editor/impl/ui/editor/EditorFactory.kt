package com.example.healthconnect.editor.impl.ui.editor

import com.example.healthconnect.models.api.domain.record.ActiveCaloriesBurned
import com.example.healthconnect.models.api.domain.record.ActivityIntensity
import com.example.healthconnect.models.api.domain.record.BasalBodyTemperature
import com.example.healthconnect.models.api.domain.record.BasalMetabolicRate
import com.example.healthconnect.models.api.domain.record.BloodGlucoseLevel
import com.example.healthconnect.models.api.domain.record.BloodPressure
import com.example.healthconnect.models.api.domain.record.BodyFat
import com.example.healthconnect.models.api.domain.record.BodyTemperature
import com.example.healthconnect.models.api.domain.record.BodyWaterMass
import com.example.healthconnect.models.api.domain.record.BoneMass
import com.example.healthconnect.models.api.domain.record.CervicalMucus
import com.example.healthconnect.models.api.domain.record.CyclingPedalingCadence
import com.example.healthconnect.models.api.domain.record.Distance
import com.example.healthconnect.models.api.domain.record.ElevationGained
import com.example.healthconnect.models.api.domain.record.ExerciseSession
import com.example.healthconnect.models.api.domain.record.FloorsClimbed
import com.example.healthconnect.models.api.domain.record.HeartRate
import com.example.healthconnect.models.api.domain.record.HeartRateVariabilityRmssd
import com.example.healthconnect.models.api.domain.record.Height
import com.example.healthconnect.models.api.domain.record.Hydration
import com.example.healthconnect.models.api.domain.record.IntermenstrualBleeding
import com.example.healthconnect.models.api.domain.record.LeanBodyMass
import com.example.healthconnect.models.api.domain.record.MenstruationFlow
import com.example.healthconnect.models.api.domain.record.MenstruationPeriod
import com.example.healthconnect.models.api.domain.record.MindfulnessSession
import com.example.healthconnect.models.api.domain.record.Nutrition
import com.example.healthconnect.models.api.domain.record.OvulationTest
import com.example.healthconnect.models.api.domain.record.OxygenSaturation
import com.example.healthconnect.models.api.domain.record.PlannedExerciseSession
import com.example.healthconnect.models.api.domain.record.Power
import com.example.healthconnect.models.api.domain.record.RespiratoryRate
import com.example.healthconnect.models.api.domain.record.RestingHeartRate
import com.example.healthconnect.models.api.domain.record.SexualActivity
import com.example.healthconnect.models.api.domain.record.SkinTemperature
import com.example.healthconnect.models.api.domain.record.SleepSession
import com.example.healthconnect.models.api.domain.record.Speed
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.models.api.domain.record.StepsCadence
import com.example.healthconnect.models.api.domain.record.TotalCaloriesBurned
import com.example.healthconnect.models.api.domain.record.Vo2Max
import com.example.healthconnect.models.api.domain.record.Weight
import com.example.healthconnect.models.api.domain.record.WheelchairPushes
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
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.PlannedExerciseSessionRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.WheelchairPushesRecord
import com.example.healthconnect.models.api.domain.record.Model
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
import com.example.healthconnect.editor.impl.ui.editor.record.CyclingPedalingCadenceEditor
import com.example.healthconnect.editor.impl.ui.editor.record.DistanceEditor
import com.example.healthconnect.editor.impl.ui.editor.record.Editor
import com.example.healthconnect.editor.impl.ui.editor.record.ElevationGainedEditor
import com.example.healthconnect.editor.impl.ui.editor.record.ExerciseSessionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.FloorsClimbedEditor
import com.example.healthconnect.editor.impl.ui.editor.record.HeartRateEditor
import com.example.healthconnect.editor.impl.ui.editor.record.HeartRateVariabilityRmssdEditor
import com.example.healthconnect.editor.impl.ui.editor.record.HeightEditor
import com.example.healthconnect.editor.impl.ui.editor.record.HydrationEditor
import com.example.healthconnect.editor.impl.ui.editor.record.IntermenstrualBleedingEditor
import com.example.healthconnect.editor.impl.ui.editor.record.LeanBodyMassEditor
import com.example.healthconnect.editor.impl.ui.editor.record.MenstruationFlowEditor
import com.example.healthconnect.editor.impl.ui.editor.record.MenstruationPeriodEditor
import com.example.healthconnect.editor.impl.ui.editor.record.MindfulnessSessionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.NutritionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.OvulationTestEditor
import com.example.healthconnect.editor.impl.ui.editor.record.OxygenSaturationEditor
import com.example.healthconnect.editor.impl.ui.editor.record.PlannedExerciseSessionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.PowerEditor
import com.example.healthconnect.editor.impl.ui.editor.record.RespiratoryRateEditor
import com.example.healthconnect.editor.impl.ui.editor.record.RestingHeartRateEditor
import com.example.healthconnect.editor.impl.ui.editor.record.SexualActivityEditor
import com.example.healthconnect.editor.impl.ui.editor.record.SkinTemperatureEditor
import com.example.healthconnect.editor.impl.ui.editor.record.SleepSessionEditor
import com.example.healthconnect.editor.impl.ui.editor.record.SpeedEditor
import com.example.healthconnect.editor.impl.ui.editor.record.StepsCadenceEditor
import com.example.healthconnect.editor.impl.ui.editor.record.StepsEditor
import com.example.healthconnect.editor.impl.ui.editor.record.TotalCaloriesBurnedEditor
import com.example.healthconnect.editor.impl.ui.editor.record.Vo2MaxEditor
import com.example.healthconnect.editor.impl.ui.editor.record.WeightEditor
import com.example.healthconnect.editor.impl.ui.editor.record.WheelchairPushesEditor
import kotlin.reflect.KClass

class EditorFactory {

    @Suppress("UNCHECKED_CAST")
    fun createByRecord(
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
        NutritionRecord::class -> NutritionEditor()
        SkinTemperatureRecord::class -> SkinTemperatureEditor()
        SleepSessionRecord::class -> SleepSessionEditor()
        StepsRecord::class -> StepsEditor()
        TotalCaloriesBurnedRecord::class -> TotalCaloriesBurnedEditor()
        WheelchairPushesRecord::class -> WheelchairPushesEditor()
        HeartRateRecord::class -> HeartRateEditor()
        CyclingPedalingCadenceRecord::class -> CyclingPedalingCadenceEditor()
        PowerRecord::class -> PowerEditor()
        SpeedRecord::class -> SpeedEditor()
        StepsCadenceRecord::class -> StepsCadenceEditor()
        else -> throw NotImplementedError()
    } as Editor<Record, Model>

    @Suppress("UNCHECKED_CAST")
    fun createByModel(
        modelKClass: KClass<out Model>,
    ): Editor<Record, Model> = when (modelKClass) {
        BasalBodyTemperature::class -> BasalBodyTemperatureEditor()
        BodyTemperature::class -> BodyTemperatureEditor()
        BasalMetabolicRate::class -> BasalMetabolicRateEditor()
        BloodGlucoseLevel::class -> BloodGlucoseEditor()
        BloodPressure::class -> BloodPressureEditor()
        BodyFat::class -> BodyFatEditor()
        BodyWaterMass::class -> BodyWaterMassEditor()
        BoneMass::class -> BoneMassEditor()
        CervicalMucus::class -> CervicalMucusEditor()
        HeartRateVariabilityRmssd::class -> HeartRateVariabilityRmssdEditor()
        Height::class -> HeightEditor()
        IntermenstrualBleeding::class -> IntermenstrualBleedingEditor()
        LeanBodyMass::class -> LeanBodyMassEditor()
        Weight::class -> WeightEditor()
        OxygenSaturation::class -> OxygenSaturationEditor()
        RespiratoryRate::class -> RespiratoryRateEditor()
        MenstruationFlow::class -> MenstruationFlowEditor()
        OvulationTest::class -> OvulationTestEditor()
        SexualActivity::class -> SexualActivityEditor()
        Vo2Max::class -> Vo2MaxEditor()
        RestingHeartRate::class -> RestingHeartRateEditor()
        ActiveCaloriesBurned::class -> ActiveCaloriesBurnedEditor()
        Distance::class -> DistanceEditor()
        ElevationGained::class -> ElevationGainedEditor()
        Hydration::class -> HydrationEditor()
        ActivityIntensity::class -> ActivityIntensityEditor()
        FloorsClimbed::class -> FloorsClimbedEditor()
        MenstruationPeriod::class -> MenstruationPeriodEditor()
        MindfulnessSession::class -> MindfulnessSessionEditor()
        ExerciseSession::class -> ExerciseSessionEditor()
        PlannedExerciseSession::class -> PlannedExerciseSessionEditor()
        Nutrition::class -> NutritionEditor()
        SkinTemperature::class -> SkinTemperatureEditor()
        SleepSession::class -> SleepSessionEditor()
        Steps::class -> StepsEditor()
        TotalCaloriesBurned::class -> TotalCaloriesBurnedEditor()
        WheelchairPushes::class -> WheelchairPushesEditor()
        HeartRate::class -> HeartRateEditor()
        CyclingPedalingCadence::class -> CyclingPedalingCadenceEditor()
        Power::class -> PowerEditor()
        Speed::class -> SpeedEditor()
        StepsCadence::class -> StepsCadenceEditor()
        else -> throw NotImplementedError()
    } as Editor<Record, Model>
}
