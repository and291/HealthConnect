package com.example.healthconnect.utilty.impl.data.mapper

import androidx.health.connect.client.records.*
import com.example.healthconnect.utilty.api.domain.record.*
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
