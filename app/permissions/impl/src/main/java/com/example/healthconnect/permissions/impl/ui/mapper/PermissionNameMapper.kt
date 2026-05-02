package com.example.healthconnect.permissions.impl.ui.mapper

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
import com.example.healthconnect.permissions.impl.R
import kotlin.reflect.KClass

class PermissionNameMapper {

    val names: Map<KClass<out Record>, Int> = mapOf(
        // Instantaneous
        BasalBodyTemperatureRecord::class to R.string.permission_name_basal_body_temperature,
        BasalMetabolicRateRecord::class to R.string.permission_name_basal_metabolic_rate,
        BloodGlucoseRecord::class to R.string.permission_name_blood_glucose,
        BloodPressureRecord::class to R.string.permission_name_blood_pressure,
        BodyFatRecord::class to R.string.permission_name_body_fat,
        BodyTemperatureRecord::class to R.string.permission_name_body_temperature,
        BodyWaterMassRecord::class to R.string.permission_name_body_water_mass,
        BoneMassRecord::class to R.string.permission_name_bone_mass,
        CervicalMucusRecord::class to R.string.permission_name_cervical_mucus,
        HeartRateVariabilityRmssdRecord::class to R.string.permission_name_heart_rate_variability_rmssd,
        HeightRecord::class to R.string.permission_name_height,
        IntermenstrualBleedingRecord::class to R.string.permission_name_intermenstrual_bleeding,
        LeanBodyMassRecord::class to R.string.permission_name_lean_body_mass,
        MenstruationFlowRecord::class to R.string.permission_name_menstruation_flow,
        OvulationTestRecord::class to R.string.permission_name_ovulation_test,
        OxygenSaturationRecord::class to R.string.permission_name_oxygen_saturation,
        RespiratoryRateRecord::class to R.string.permission_name_respiratory_rate,
        RestingHeartRateRecord::class to R.string.permission_name_resting_heart_rate,
        SexualActivityRecord::class to R.string.permission_name_sexual_activity,
        Vo2MaxRecord::class to R.string.permission_name_vo2_max,
        WeightRecord::class to R.string.permission_name_weight,
        // Interval
        ActiveCaloriesBurnedRecord::class to R.string.permission_name_active_calories_burned,
        ActivityIntensityRecord::class to R.string.permission_name_activity_intensity,
        DistanceRecord::class to R.string.permission_name_distance,
        ElevationGainedRecord::class to R.string.permission_name_elevation_gained,
        ExerciseSessionRecord::class to R.string.permission_name_exercise_session,
        FloorsClimbedRecord::class to R.string.permission_name_floors_climbed,
        HydrationRecord::class to R.string.permission_name_hydration,
        MenstruationPeriodRecord::class to R.string.permission_name_menstruation_period,
        MindfulnessSessionRecord::class to R.string.permission_name_mindfulness_session,
        NutritionRecord::class to R.string.permission_name_nutrition,
        PlannedExerciseSessionRecord::class to R.string.permission_name_planned_exercise_session,
        SkinTemperatureRecord::class to R.string.permission_name_skin_temperature,
        SleepSessionRecord::class to R.string.permission_name_sleep_session,
        StepsRecord::class to R.string.permission_name_steps,
        TotalCaloriesBurnedRecord::class to R.string.permission_name_total_calories_burned,
        WheelchairPushesRecord::class to R.string.permission_name_wheelchair_pushes,
        // Series
        CyclingPedalingCadenceRecord::class to R.string.permission_name_cycling_pedaling_cadence,
        HeartRateRecord::class to R.string.permission_name_heart_rate,
        PowerRecord::class to R.string.permission_name_power,
        SpeedRecord::class to R.string.permission_name_speed,
        StepsCadenceRecord::class to R.string.permission_name_steps_cadence,
    )

    @StringRes
    fun nameRes(recordClass: KClass<out Record>): Int = requireNotNull(names[recordClass]) {
        "No name resource for record class $recordClass"
    }
}
