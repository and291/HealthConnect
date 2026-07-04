package com.example.healthconnect.utilty.impl.domain

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

internal object SupportedModels {

    // time + zoneOffset
    val instantaneous: List<KClass<out Model>> = listOf(
        BasalBodyTemperature::class,
        BasalMetabolicRate::class,
        BloodGlucoseLevel::class,
        BloodPressure::class,
        BodyFat::class,
        BodyTemperature::class,
        BodyWaterMass::class,
        BoneMass::class,
        CervicalMucus::class,
        HeartRateVariabilityRmssd::class,
        Height::class,
        IntermenstrualBleeding::class,
        LeanBodyMass::class,
        MenstruationFlow::class,
        OvulationTest::class,
        OxygenSaturation::class,
        RespiratoryRate::class,
        RestingHeartRate::class,
        SexualActivity::class,
        Vo2Max::class,
        Weight::class,
    )

    // start, endTime + offset
    val interval: List<KClass<out Model>> = listOf(
        ActiveCaloriesBurned::class,
        ActivityIntensity::class,
        Distance::class,
        ElevationGained::class,
        ExerciseSession::class,
        FloorsClimbed::class,
        Hydration::class,
        MenstruationPeriod::class,
        MindfulnessSession::class,
        Nutrition::class,
        PlannedExerciseSession::class,
        SkinTemperature::class,
        SleepSession::class,
        Steps::class,
        TotalCaloriesBurned::class,
        WheelchairPushes::class,
    )

    // list of T
    val series: List<KClass<out Model>> = listOf(
        CyclingPedalingCadence::class,
        HeartRate::class,
        Power::class,
        Speed::class,
        StepsCadence::class,
    )

    val all = instantaneous + interval + series
}
