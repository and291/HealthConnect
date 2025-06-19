package com.example.healthconnect.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.WheelchairPushesRecord
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass

class SdkAvailableViewModel(
) : ViewModel() {

    private val _state by mutableStateOf(State.RecordTypes())

    val state: State
        get() = _state

    sealed class State {

        @OptIn(ExperimentalMindfulnessSessionApi::class)
        data class RecordTypes(
            val availableTypes: List<KClass<*>> = listOf<KClass<*>>(
                ActiveCaloriesBurnedRecord::class,
                BasalMetabolicRateRecord::class,
                BloodPressureRecord::class,
                CyclingPedalingCadenceRecord::class,
                DistanceRecord::class,
                ElevationGainedRecord::class,
                ExerciseSessionRecord::class,
                FloorsClimbedRecord::class,
                HeartRateRecord::class,
                HeightRecord::class,
                HydrationRecord::class,
                MindfulnessSessionRecord::class,
                NutritionRecord::class,
                PowerRecord::class,
                RestingHeartRateRecord::class,
                SkinTemperatureRecord::class,
                SleepSessionRecord::class,
                SpeedRecord::class,
                StepsRecord::class,
                StepsCadenceRecord::class,
                TotalCaloriesBurnedRecord::class,
                WeightRecord::class,
                WheelchairPushesRecord::class,
            )
        ) : State()
    }
}
