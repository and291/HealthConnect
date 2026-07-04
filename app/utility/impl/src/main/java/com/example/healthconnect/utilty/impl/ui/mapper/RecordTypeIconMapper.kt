package com.example.healthconnect.utilty.impl.ui.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthconnect.utilty.api.domain.record.*
import kotlin.reflect.KClass

class RecordTypeIconMapper {

    fun icon(type: KClass<out Model>): ImageVector = requireNotNull(icons[type]) {
        "No icon for record type ${type.simpleName}"
    }

    private val icons: Map<KClass<out Model>, ImageVector> = mapOf(
        // Instantaneous
        BasalBodyTemperature::class to Icons.Default.Thermostat,
        BasalMetabolicRate::class to Icons.Default.LocalFireDepartment,
        BloodGlucoseLevel::class to Icons.Default.Bloodtype,
        BloodPressure::class to Icons.Default.MonitorHeart,
        BodyFat::class to Icons.Default.Person,
        BodyTemperature::class to Icons.Default.Thermostat,
        BodyWaterMass::class to Icons.Default.Water,
        BoneMass::class to Icons.Default.Accessibility,
        CervicalMucus::class to Icons.Default.Opacity,
        HeartRateVariabilityRmssd::class to Icons.AutoMirrored.Filled.ShowChart,
        Height::class to Icons.Default.Height,
        IntermenstrualBleeding::class to Icons.Default.Opacity,
        LeanBodyMass::class to Icons.Default.FitnessCenter,
        MenstruationFlow::class to Icons.Default.Water,
        OvulationTest::class to Icons.Default.Science,
        OxygenSaturation::class to Icons.Default.Air,
        RespiratoryRate::class to Icons.Default.Air,
        RestingHeartRate::class to Icons.Default.Favorite,
        SexualActivity::class to Icons.Default.Favorite,
        Vo2Max::class to Icons.AutoMirrored.Filled.DirectionsRun,
        Weight::class to Icons.Default.Scale,
        // Interval
        ActiveCaloriesBurned::class to Icons.Default.LocalFireDepartment,
        ActivityIntensity::class to Icons.AutoMirrored.Filled.DirectionsRun,
        Distance::class to Icons.Default.Straighten,
        ElevationGained::class to Icons.Default.Terrain,
        ExerciseSession::class to Icons.Default.FitnessCenter,
        FloorsClimbed::class to Icons.Default.Stairs,
        Hydration::class to Icons.Default.LocalDrink,
        MenstruationPeriod::class to Icons.Default.CalendarToday,
        MindfulnessSession::class to Icons.Default.SelfImprovement,
        Nutrition::class to Icons.Default.Restaurant,
        PlannedExerciseSession::class to Icons.AutoMirrored.Filled.EventNote,
        SkinTemperature::class to Icons.Default.Thermostat,
        SleepSession::class to Icons.Default.Bedtime,
        Steps::class to Icons.AutoMirrored.Filled.DirectionsWalk,
        TotalCaloriesBurned::class to Icons.Default.LocalFireDepartment,
        WheelchairPushes::class to Icons.AutoMirrored.Filled.Accessible,
        // Series
        CyclingPedalingCadence::class to Icons.AutoMirrored.Filled.DirectionsBike,
        HeartRate::class to Icons.Default.Favorite,
        Power::class to Icons.Default.Bolt,
        Speed::class to Icons.Default.Speed,
        StepsCadence::class to Icons.AutoMirrored.Filled.DirectionsWalk,
    )
}
