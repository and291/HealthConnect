package com.example.healthconnect.utilty.impl.ui.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.ui.graphics.vector.ImageVector
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
import kotlin.reflect.KClass

class RecordTypeIconMapper {

    fun icon(type: KClass<*>): ImageVector = requireNotNull(icons[type]) {
        "No icon for record type ${type.simpleName}"
    }

    private val icons: Map<KClass<*>, ImageVector> = mapOf(
        // Instantaneous
        BasalBodyTemperatureRecord::class to Icons.Default.Thermostat,
        BasalMetabolicRateRecord::class to Icons.Default.LocalFireDepartment,
        BloodGlucoseRecord::class to Icons.Default.Bloodtype,
        BloodPressureRecord::class to Icons.Default.MonitorHeart,
        BodyFatRecord::class to Icons.Default.Person,
        BodyTemperatureRecord::class to Icons.Default.Thermostat,
        BodyWaterMassRecord::class to Icons.Default.Water,
        BoneMassRecord::class to Icons.Default.Accessibility,
        CervicalMucusRecord::class to Icons.Default.Opacity,
        HeartRateVariabilityRmssdRecord::class to Icons.AutoMirrored.Filled.ShowChart,
        HeightRecord::class to Icons.Default.Height,
        IntermenstrualBleedingRecord::class to Icons.Default.Opacity,
        LeanBodyMassRecord::class to Icons.Default.FitnessCenter,
        MenstruationFlowRecord::class to Icons.Default.Water,
        OvulationTestRecord::class to Icons.Default.Science,
        OxygenSaturationRecord::class to Icons.Default.Air,
        RespiratoryRateRecord::class to Icons.Default.Air,
        RestingHeartRateRecord::class to Icons.Default.Favorite,
        SexualActivityRecord::class to Icons.Default.Favorite,
        Vo2MaxRecord::class to Icons.AutoMirrored.Filled.DirectionsRun,
        WeightRecord::class to Icons.Default.Scale,
        // Interval
        ActiveCaloriesBurnedRecord::class to Icons.Default.LocalFireDepartment,
        ActivityIntensityRecord::class to Icons.AutoMirrored.Filled.DirectionsRun,
        DistanceRecord::class to Icons.Default.Straighten,
        ElevationGainedRecord::class to Icons.Default.Terrain,
        ExerciseSessionRecord::class to Icons.Default.FitnessCenter,
        FloorsClimbedRecord::class to Icons.Default.Stairs,
        HydrationRecord::class to Icons.Default.LocalDrink,
        MenstruationPeriodRecord::class to Icons.Default.CalendarToday,
        MindfulnessSessionRecord::class to Icons.Default.SelfImprovement,
        NutritionRecord::class to Icons.Default.Restaurant,
        PlannedExerciseSessionRecord::class to Icons.AutoMirrored.Filled.EventNote,
        SkinTemperatureRecord::class to Icons.Default.Thermostat,
        SleepSessionRecord::class to Icons.Default.Bedtime,
        StepsRecord::class to Icons.AutoMirrored.Filled.DirectionsWalk,
        TotalCaloriesBurnedRecord::class to Icons.Default.LocalFireDepartment,
        WheelchairPushesRecord::class to Icons.AutoMirrored.Filled.Accessible,
        // Series
        CyclingPedalingCadenceRecord::class to Icons.AutoMirrored.Filled.DirectionsBike,
        HeartRateRecord::class to Icons.Default.Favorite,
        PowerRecord::class to Icons.Default.Bolt,
        SpeedRecord::class to Icons.Default.Speed,
        StepsCadenceRecord::class to Icons.AutoMirrored.Filled.DirectionsWalk,
    )
}
