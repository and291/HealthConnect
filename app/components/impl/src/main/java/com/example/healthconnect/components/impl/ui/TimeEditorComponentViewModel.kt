package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.TimeComponentModel
import com.example.healthconnect.components.api.ui.model.TimeModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

internal class TimeEditorComponentViewModel(
    model: TimeComponentModel,
) : ViewModel() {

    private var _state by mutableStateOf(model)

    val state: TimeComponentModel
        get() = _state

    private val _effect = MutableStateFlow<Effect?>(null)

    private var onTimeChangedJob: Job? = null

    fun onEvent(event: Event) {
        onTimeChangedJob?.takeIf { it.isActive }?.cancel() //TODO check if this threadsafe?!
        onTimeChangedJob = viewModelScope.launch {
            val model = when (event) {
                is Event.OnInstantaneousTimeChanged -> TimeComponentModel.Instantaneous(
                    time = timeModel(
                        instantValue = event.value,
                        zoneValue = event.zoneId,
                    )
                )

                is Event.OnEndTimeChanged -> (_state as TimeComponentModel.Interval).copy(
                    end = timeModel(
                        instantValue = event.value,
                        zoneValue = event.zoneId,
                    )
                )

                is Event.OnStartTimeChanged -> (_state as TimeComponentModel.Interval).copy(
                    start = timeModel(
                        instantValue = event.value,
                        zoneValue = event.zoneId,
                    )
                )
            }

            if (this.isActive) {
                _state = model
            }
        }
    }

    private suspend fun zoneOffset(inputString: String): Result<ZoneOffset> = runCatching {
        val startZoneTrace = System.currentTimeMillis()
        ZoneOffset.of(inputString).also {
            Log.d(
                this::class.simpleName,
                "ZoneOffset handling took ${System.currentTimeMillis() - startZoneTrace} ms"
            )
        }
    }.also {
        if (it.isFailure) {
            val zoneErrorMessage = "Failed to select ZoneId: $inputString"
            Log.e(this::class.simpleName, zoneErrorMessage, it.exceptionOrNull())
            _effect.emit(Effect.ZoneSelectionError(zoneErrorMessage))
        }
    }

    private suspend fun timeModel(
        instantValue: String,
        zoneValue: String?,
    ): TimeModel = try {
        val startTimeTrace = System.currentTimeMillis()
        TimeModel.Valid(
            instant = Instant.parse(instantValue).also {
                Log.d(
                    this::class.simpleName,
                    "Instant.parse took ${System.currentTimeMillis() - startTimeTrace} ms"
                )
            },
            zoneOffset = zoneValue?.let { zoneOffset(it).getOrNull() },
        )
    } catch (e: DateTimeParseException) {
        TimeModel.Invalid(
            input = instantValue,
            zoneOffset = zoneValue?.let { zoneOffset(it).getOrNull() },
        ).also {
            Log.e(this::class.simpleName, it.getParseErrorMessage(), e)
        }
    }

    sealed class Effect {

        data class ZoneSelectionError(
            val message: String,
        ) : Effect()
    }

    sealed class Event {

        abstract val value: String
        abstract val zoneId: String?

        data class OnInstantaneousTimeChanged(
            override val value: String,
            override val zoneId: String?,
        ) : Event()

        data class OnStartTimeChanged(
            override val value: String,
            override val zoneId: String?,
        ) : Event()

        data class OnEndTimeChanged(
            override val value: String,
            override val zoneId: String?,
        ) : Event()
    }

    companion object {

        val TIME_MODEL_KEY: CreationExtras.Key<TimeComponentModel> = CreationExtras.Companion.Key()
    }
}