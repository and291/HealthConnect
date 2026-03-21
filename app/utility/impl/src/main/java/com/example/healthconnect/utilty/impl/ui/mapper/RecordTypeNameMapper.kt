package com.example.healthconnect.utilty.impl.ui.mapper

import androidx.annotation.StringRes
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
import com.example.healthconnect.utilty.impl.R
import kotlin.reflect.KClass

class RecordTypeNameMapper {

    @StringRes
    fun nameRes(type: KClass<*>): Int = requireNotNull(names[type]) {
        "No string resource for record type ${type.simpleName}"
    }

    private val names: Map<KClass<*>, Int> = mapOf(
        // Instantaneous
        BasalBodyTemperatureRecord::class to R.string.record_type_basal_body_temperature,
        BasalMetabolicRateRecord::class to R.string.record_type_basal_metabolic_rate,
        BloodGlucoseRecord::class to R.string.record_type_blood_glucose,
        BloodPressureRecord::class to R.string.record_type_blood_pressure,
        BodyFatRecord::class to R.string.record_type_body_fat,
        BodyTemperatureRecord::class to R.string.record_type_body_temperature,
        BodyWaterMassRecord::class to R.string.record_type_body_water_mass,
        BoneMassRecord::class to R.string.record_type_bone_mass,
        CervicalMucusRecord::class to R.string.record_type_cervical_mucus,
        HeartRateVariabilityRmssdRecord::class to R.string.record_type_heart_rate_variability_rmssd,
        HeightRecord::class to R.string.record_type_height,
        IntermenstrualBleedingRecord::class to R.string.record_type_intermenstrual_bleeding,
        LeanBodyMassRecord::class to R.string.record_type_lean_body_mass,
        MenstruationFlowRecord::class to R.string.record_type_menstruation_flow,
        OvulationTestRecord::class to R.string.record_type_ovulation_test,
        OxygenSaturationRecord::class to R.string.record_type_oxygen_saturation,
        RespiratoryRateRecord::class to R.string.record_type_respiratory_rate,
        RestingHeartRateRecord::class to R.string.record_type_resting_heart_rate,
        SexualActivityRecord::class to R.string.record_type_sexual_activity,
        Vo2MaxRecord::class to R.string.record_type_vo2_max,
        WeightRecord::class to R.string.record_type_weight,
        // Interval
        ActiveCaloriesBurnedRecord::class to R.string.record_type_active_calories_burned,
        ActivityIntensityRecord::class to R.string.record_type_activity_intensity,
        DistanceRecord::class to R.string.record_type_distance,
        ElevationGainedRecord::class to R.string.record_type_elevation_gained,
        ExerciseSessionRecord::class to R.string.record_type_exercise_session,
        FloorsClimbedRecord::class to R.string.record_type_floors_climbed,
        HydrationRecord::class to R.string.record_type_hydration,
        MenstruationPeriodRecord::class to R.string.record_type_menstruation_period,
        MindfulnessSessionRecord::class to R.string.record_type_mindfulness_session,
        NutritionRecord::class to R.string.record_type_nutrition,
        PlannedExerciseSessionRecord::class to R.string.record_type_planned_exercise_session,
        SkinTemperatureRecord::class to R.string.record_type_skin_temperature,
        SleepSessionRecord::class to R.string.record_type_sleep_session,
        StepsRecord::class to R.string.record_type_steps,
        TotalCaloriesBurnedRecord::class to R.string.record_type_total_calories_burned,
        WheelchairPushesRecord::class to R.string.record_type_wheelchair_pushes,
        // Series
        CyclingPedalingCadenceRecord::class to R.string.record_type_cycling_pedaling_cadence,
        HeartRateRecord::class to R.string.record_type_heart_rate,
        PowerRecord::class to R.string.record_type_power,
        SpeedRecord::class to R.string.record_type_speed,
        StepsCadenceRecord::class to R.string.record_type_steps_cadence,
    )
}
