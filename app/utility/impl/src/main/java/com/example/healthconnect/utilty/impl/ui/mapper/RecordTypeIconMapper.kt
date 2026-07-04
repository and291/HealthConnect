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
import com.example.healthconnect.utilty.api.domain.record.ActiveCaloriesBurned
import com.example.healthconnect.utilty.api.domain.record.ActivityIntensity
import com.example.healthconnect.utilty.api.domain.record.BasalBodyTemperature
import com.example.healthconnect.utilty.api.domain.record.BasalMetabolicRate
import com.example.healthconnect.utilty.api.domain.record.BloodGlucoseLevel
import com.example.healthconnect.utilty.api.domain.record.BloodPressure
import com.example.healthconnect.utilty.api.domain.record.BodyFat
import com.example.healthconnect.utilty.api.domain.record.BodyTemperature
import com.example.healthconnect.utilty.api.domain.record.BodyWaterMass
import com.example.healthconnect.utilty.api.domain.record.BoneMass
import com.example.healthconnect.utilty.api.domain.record.CervicalMucus
import com.example.healthconnect.utilty.api.domain.record.CyclingPedalingCadence
import com.example.healthconnect.utilty.api.domain.record.Distance
import com.example.healthconnect.utilty.api.domain.record.ElevationGained
import com.example.healthconnect.utilty.api.domain.record.ExerciseSession
import com.example.healthconnect.utilty.api.domain.record.FloorsClimbed
import com.example.healthconnect.utilty.api.domain.record.HeartRate
import com.example.healthconnect.utilty.api.domain.record.HeartRateVariabilityRmssd
import com.example.healthconnect.utilty.api.domain.record.Height
import com.example.healthconnect.utilty.api.domain.record.Hydration
import com.example.healthconnect.utilty.api.domain.record.IntermenstrualBleeding
import com.example.healthconnect.utilty.api.domain.record.LeanBodyMass
import com.example.healthconnect.utilty.api.domain.record.MenstruationFlow
import com.example.healthconnect.utilty.api.domain.record.MenstruationPeriod
import com.example.healthconnect.utilty.api.domain.record.MindfulnessSession
import com.example.healthconnect.utilty.api.domain.record.Model
import com.example.healthconnect.utilty.api.domain.record.Nutrition
import com.example.healthconnect.utilty.api.domain.record.OvulationTest
import com.example.healthconnect.utilty.api.domain.record.OxygenSaturation
import com.example.healthconnect.utilty.api.domain.record.PlannedExerciseSession
import com.example.healthconnect.utilty.api.domain.record.Power
import com.example.healthconnect.utilty.api.domain.record.RespiratoryRate
import com.example.healthconnect.utilty.api.domain.record.RestingHeartRate
import com.example.healthconnect.utilty.api.domain.record.SexualActivity
import com.example.healthconnect.utilty.api.domain.record.SkinTemperature
import com.example.healthconnect.utilty.api.domain.record.SleepSession
import com.example.healthconnect.utilty.api.domain.record.Speed
import com.example.healthconnect.utilty.api.domain.record.Steps
import com.example.healthconnect.utilty.api.domain.record.StepsCadence
import com.example.healthconnect.utilty.api.domain.record.TotalCaloriesBurned
import com.example.healthconnect.utilty.api.domain.record.Vo2Max
import com.example.healthconnect.utilty.api.domain.record.Weight
import com.example.healthconnect.utilty.api.domain.record.WheelchairPushes
import kotlin.reflect.KClass

class RecordTypeIconMapper {

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

    fun icon(type: KClass<out Model>): ImageVector = requireNotNull(icons[type]) {
        "No icon for record type ${type.simpleName}"
    }

    @Suppress("unused")
    inline fun <reified M : Model> toIcon(): ImageVector = icon(M::class)
}
