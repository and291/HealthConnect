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
import androidx.health.connect.client.records.metadata.Metadata
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.domain.model.Result
import com.example.healthconnect.domain.usecase.Insert
import com.example.healthconnect.ui.screen.SdkAvailableViewModel.Effect.RequestSinglePermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import kotlin.reflect.KClass

class SdkAvailableViewModel(
    private val insert: Insert,
) : ViewModel() {

    private val _state by mutableStateOf(State.RecordTypes())
    private val _effect = MutableStateFlow<Effect?>(null)

    val state: State
        get() = _state
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    fun insertSteps() = viewModelScope.launch {
        val stepsRecord = StepsRecord(
            count = 120,
            startTime = Instant.now().minusSeconds(120),
            endTime = Instant.now(),
            startZoneOffset = ZoneOffset.UTC,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
        )
        val result = insert(stepsRecord)

        val effect = when (result) {
            is Result.IoException -> TODO()
            is Result.IpcFailure -> TODO()
            is Result.PermissionRequired -> RequestSinglePermission(result.sdkPermission)
            is Result.Success -> Effect.StepsInsertedSuccessfully(result.toString())
            is Result.UnhandledException -> TODO()
            is Result.UnpermittedAccess -> TODO()
        }

        _effect.emit(effect)
    }

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

    sealed class Effect {

        data class RequestSinglePermission(
            val sdkPermission: String
        ) : Effect()

        data class StepsInsertedSuccessfully(
            val result: String
        ) : Effect()
    }
}
