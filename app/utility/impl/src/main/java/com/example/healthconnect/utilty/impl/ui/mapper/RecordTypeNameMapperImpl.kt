package com.example.healthconnect.utilty.impl.ui.mapper

import androidx.annotation.StringRes
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
import com.example.healthconnect.utilty.api.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.R
import kotlin.reflect.KClass

class RecordTypeNameMapperImpl : RecordTypeNameMapper {

    @StringRes
    override fun nameRes(type: KClass<out Model>): Int = requireNotNull(names[type]) {
        "No string resource for record type ${type.simpleName}"
    }

    private val names: Map<KClass<out Model>, Int> = mapOf(
        // Instantaneous
        BasalBodyTemperature::class to R.string.record_type_basal_body_temperature,
        BasalMetabolicRate::class to R.string.record_type_basal_metabolic_rate,
        BloodGlucoseLevel::class to R.string.record_type_blood_glucose,
        BloodPressure::class to R.string.record_type_blood_pressure,
        BodyFat::class to R.string.record_type_body_fat,
        BodyTemperature::class to R.string.record_type_body_temperature,
        BodyWaterMass::class to R.string.record_type_body_water_mass,
        BoneMass::class to R.string.record_type_bone_mass,
        CervicalMucus::class to R.string.record_type_cervical_mucus,
        HeartRateVariabilityRmssd::class to R.string.record_type_heart_rate_variability_rmssd,
        Height::class to R.string.record_type_height,
        IntermenstrualBleeding::class to R.string.record_type_intermenstrual_bleeding,
        LeanBodyMass::class to R.string.record_type_lean_body_mass,
        MenstruationFlow::class to R.string.record_type_menstruation_flow,
        OvulationTest::class to R.string.record_type_ovulation_test,
        OxygenSaturation::class to R.string.record_type_oxygen_saturation,
        RespiratoryRate::class to R.string.record_type_respiratory_rate,
        RestingHeartRate::class to R.string.record_type_resting_heart_rate,
        SexualActivity::class to R.string.record_type_sexual_activity,
        Vo2Max::class to R.string.record_type_vo2_max,
        Weight::class to R.string.record_type_weight,
        // Interval
        ActiveCaloriesBurned::class to R.string.record_type_active_calories_burned,
        ActivityIntensity::class to R.string.record_type_activity_intensity,
        Distance::class to R.string.record_type_distance,
        ElevationGained::class to R.string.record_type_elevation_gained,
        ExerciseSession::class to R.string.record_type_exercise_session,
        FloorsClimbed::class to R.string.record_type_floors_climbed,
        Hydration::class to R.string.record_type_hydration,
        MenstruationPeriod::class to R.string.record_type_menstruation_period,
        MindfulnessSession::class to R.string.record_type_mindfulness_session,
        Nutrition::class to R.string.record_type_nutrition,
        PlannedExerciseSession::class to R.string.record_type_planned_exercise_session,
        SkinTemperature::class to R.string.record_type_skin_temperature,
        SleepSession::class to R.string.record_type_sleep_session,
        Steps::class to R.string.record_type_steps,
        TotalCaloriesBurned::class to R.string.record_type_total_calories_burned,
        WheelchairPushes::class to R.string.record_type_wheelchair_pushes,
        // Series
        CyclingPedalingCadence::class to R.string.record_type_cycling_pedaling_cadence,
        HeartRate::class to R.string.record_type_heart_rate,
        Power::class to R.string.record_type_power,
        Speed::class to R.string.record_type_speed,
        StepsCadence::class to R.string.record_type_steps_cadence,
    )
}
