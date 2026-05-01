package com.example.healthconnect.utilty.impl.domain.mapper

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
import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionType
import kotlin.reflect.KClass

class RecordTypePermissionMapper {

    fun readPermission(recordType: KClass<out Model>): HealthPermission {
        val suffix = requireNotNull(typeToSuffix[recordType]) {
            "No READ permission mapped for ${recordType.simpleName}"
        }
        return HealthPermission(
            permissionString = "android.permission.health.READ_$suffix",
            type = PermissionType.Read,
        )
    }

    private val typeToSuffix: Map<KClass<out Model>, String> = mapOf(
        // Instantaneous
        BasalBodyTemperature::class      to "BASAL_BODY_TEMPERATURE",
        BasalMetabolicRate::class        to "BASAL_METABOLIC_RATE",
        BloodGlucoseLevel::class         to "BLOOD_GLUCOSE",
        BloodPressure::class             to "BLOOD_PRESSURE",
        BodyFat::class                   to "BODY_FAT",
        BodyTemperature::class           to "BODY_TEMPERATURE",
        BodyWaterMass::class             to "BODY_WATER_MASS",
        BoneMass::class                  to "BONE_MASS",
        CervicalMucus::class             to "CERVICAL_MUCUS",
        HeartRateVariabilityRmssd::class to "HEART_RATE_VARIABILITY",
        Height::class                    to "HEIGHT",
        IntermenstrualBleeding::class    to "INTERMENSTRUAL_BLEEDING",
        LeanBodyMass::class              to "LEAN_BODY_MASS",
        MenstruationFlow::class          to "MENSTRUATION",
        OvulationTest::class             to "OVULATION_TEST",
        OxygenSaturation::class          to "OXYGEN_SATURATION",
        RespiratoryRate::class           to "RESPIRATORY_RATE",
        RestingHeartRate::class          to "RESTING_HEART_RATE",
        SexualActivity::class            to "SEXUAL_ACTIVITY",
        Vo2Max::class                    to "VO2_MAX",
        Weight::class                    to "WEIGHT",
        // Interval
        ActiveCaloriesBurned::class      to "ACTIVE_CALORIES_BURNED",
        ActivityIntensity::class         to "ACTIVITY_INTENSITY",
        Distance::class                  to "DISTANCE",
        ElevationGained::class           to "ELEVATION_GAINED",
        ExerciseSession::class           to "EXERCISE",
        FloorsClimbed::class             to "FLOORS_CLIMBED",
        Hydration::class                 to "HYDRATION",
        MenstruationPeriod::class        to "MENSTRUATION",
        MindfulnessSession::class        to "MINDFULNESS",
        Nutrition::class                 to "NUTRITION",
        PlannedExerciseSession::class    to "PLANNED_EXERCISE",
        SkinTemperature::class           to "SKIN_TEMPERATURE",
        SleepSession::class              to "SLEEP",
        Steps::class                     to "STEPS",
        TotalCaloriesBurned::class       to "TOTAL_CALORIES_BURNED",
        WheelchairPushes::class          to "WHEELCHAIR_PUSHES",
        // Series
        CyclingPedalingCadence::class    to "EXERCISE",
        HeartRate::class                 to "HEART_RATE",
        Power::class                     to "POWER",
        Speed::class                     to "SPEED",
        StepsCadence::class              to "STEPS",
    )
}
