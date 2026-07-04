package com.example.healthconnect.utilty.impl.domain

import com.example.healthconnect.utilty.api.domain.record.*
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
