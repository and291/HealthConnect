package com.example.healthconnect.utilty.impl.domain

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
import com.example.healthconnect.models.api.domain.record.Model
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
