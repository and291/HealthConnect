package com.example.healthconnect.utilty.impl.ui.mapper

import androidx.annotation.StringRes
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
import com.example.healthconnect.utilty.impl.R
import kotlin.reflect.KClass

class RecordTypeNameMapper {

    @StringRes
    fun nameRes(type: KClass<out Model>): Int = requireNotNull(names[type]) {
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
