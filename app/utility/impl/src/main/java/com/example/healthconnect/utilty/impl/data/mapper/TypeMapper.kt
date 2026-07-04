package com.example.healthconnect.utilty.impl.data.mapper

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
import com.example.healthconnect.utilty.api.record.ActiveCaloriesBurned
import com.example.healthconnect.utilty.api.record.ActivityIntensity
import com.example.healthconnect.utilty.api.record.BasalBodyTemperature
import com.example.healthconnect.utilty.api.record.BasalMetabolicRate
import com.example.healthconnect.utilty.api.record.BloodGlucoseLevel
import com.example.healthconnect.utilty.api.record.BloodPressure
import com.example.healthconnect.utilty.api.record.BodyFat
import com.example.healthconnect.utilty.api.record.BodyTemperature
import com.example.healthconnect.utilty.api.record.BodyWaterMass
import com.example.healthconnect.utilty.api.record.BoneMass
import com.example.healthconnect.utilty.api.record.CervicalMucus
import com.example.healthconnect.utilty.api.record.CyclingPedalingCadence
import com.example.healthconnect.utilty.api.record.Distance
import com.example.healthconnect.utilty.api.record.ElevationGained
import com.example.healthconnect.utilty.api.record.ExerciseSession
import com.example.healthconnect.utilty.api.record.FloorsClimbed
import com.example.healthconnect.utilty.api.record.HeartRate
import com.example.healthconnect.utilty.api.record.HeartRateVariabilityRmssd
import com.example.healthconnect.utilty.api.record.Height
import com.example.healthconnect.utilty.api.record.Hydration
import com.example.healthconnect.utilty.api.record.IntermenstrualBleeding
import com.example.healthconnect.utilty.api.record.LeanBodyMass
import com.example.healthconnect.utilty.api.record.MenstruationFlow
import com.example.healthconnect.utilty.api.record.MenstruationPeriod
import com.example.healthconnect.utilty.api.record.MindfulnessSession
import com.example.healthconnect.utilty.api.record.Model
import com.example.healthconnect.utilty.api.record.Nutrition
import com.example.healthconnect.utilty.api.record.OvulationTest
import com.example.healthconnect.utilty.api.record.OxygenSaturation
import com.example.healthconnect.utilty.api.record.PlannedExerciseSession
import com.example.healthconnect.utilty.api.record.Power
import com.example.healthconnect.utilty.api.record.RespiratoryRate
import com.example.healthconnect.utilty.api.record.RestingHeartRate
import com.example.healthconnect.utilty.api.record.SexualActivity
import com.example.healthconnect.utilty.api.record.SkinTemperature
import com.example.healthconnect.utilty.api.record.SleepSession
import com.example.healthconnect.utilty.api.record.Speed
import com.example.healthconnect.utilty.api.record.Steps
import com.example.healthconnect.utilty.api.record.StepsCadence
import com.example.healthconnect.utilty.api.record.TotalCaloriesBurned
import com.example.healthconnect.utilty.api.record.Vo2Max
import com.example.healthconnect.utilty.api.record.Weight
import com.example.healthconnect.utilty.api.record.WheelchairPushes
import kotlin.reflect.KClass

class TypeMapper {

    fun toRecord(type: KClass<out Model>): KClass<out Record> = requireNotNull(mapping[type]) {
        "No type for model ${type.simpleName}"
    }

    private val mapping: Map<KClass<out Model>, KClass<out Record>> = mapOf(
        // Instantaneous
        BasalBodyTemperature::class to BasalBodyTemperatureRecord::class,
        BasalMetabolicRate::class to BasalMetabolicRateRecord::class,
        BloodGlucoseLevel::class to BloodGlucoseRecord::class,
        BloodPressure::class to BloodPressureRecord::class,
        BodyFat::class to BodyFatRecord::class,
        BodyTemperature::class to BodyTemperatureRecord::class,
        BodyWaterMass::class to BodyWaterMassRecord::class,
        BoneMass::class to BoneMassRecord::class,
        CervicalMucus::class to CervicalMucusRecord::class,
        HeartRateVariabilityRmssd::class to HeartRateVariabilityRmssdRecord::class,
        Height::class to HeightRecord::class,
        IntermenstrualBleeding::class to IntermenstrualBleedingRecord::class,
        LeanBodyMass::class to LeanBodyMassRecord::class,
        MenstruationFlow::class to MenstruationFlowRecord::class,
        OvulationTest::class to OvulationTestRecord::class,
        OxygenSaturation::class to OxygenSaturationRecord::class,
        RespiratoryRate::class to RespiratoryRateRecord::class,
        RestingHeartRate::class to RestingHeartRateRecord::class,
        SexualActivity::class to SexualActivityRecord::class,
        Vo2Max::class to Vo2MaxRecord::class,
        Weight::class to WeightRecord::class,
        // Interval
        ActiveCaloriesBurned::class to ActiveCaloriesBurnedRecord::class,
        ActivityIntensity::class to ActivityIntensityRecord::class,
        Distance::class to DistanceRecord::class,
        ElevationGained::class to ElevationGainedRecord::class,
        ExerciseSession::class to ExerciseSessionRecord::class,
        FloorsClimbed::class to FloorsClimbedRecord::class,
        Hydration::class to HydrationRecord::class,
        MenstruationPeriod::class to MenstruationPeriodRecord::class,
        MindfulnessSession::class to MindfulnessSessionRecord::class,
        Nutrition::class to NutritionRecord::class,
        PlannedExerciseSession::class to PlannedExerciseSessionRecord::class,
        SkinTemperature::class to SkinTemperatureRecord::class,
        SleepSession::class to SleepSessionRecord::class,
        Steps::class to StepsRecord::class,
        TotalCaloriesBurned::class to TotalCaloriesBurnedRecord::class,
        WheelchairPushes::class to WheelchairPushesRecord::class,
        // Series
        CyclingPedalingCadence::class to CyclingPedalingCadenceRecord::class,
        HeartRate::class to HeartRateRecord::class,
        Power::class to PowerRecord::class,
        Speed::class to SpeedRecord::class,
        StepsCadence::class to StepsCadenceRecord::class,
    )
}
