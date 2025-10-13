package com.example.healthconnect.ui.screen.component

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.ui.screen.component.model.TimeComponentModel
import com.example.healthconnect.ui.screen.component.model.TimeComponentModel.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeParseException

class TimeComponentViewModel(
    timeComponentModel: TimeComponentModel
) : ViewModel() {

    private var _state by mutableStateOf(timeComponentModel)

    val state: TimeComponentModel
        get() = _state

    private val _effect = MutableStateFlow<Effect?>(null)

    private var onTimeChangedJob: Job? = null

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnTimeChanged -> {
                onTimeChangedJob?.takeIf { it.isActive }?.cancel() //TODO check if this threadsafe?!
                onTimeChangedJob = viewModelScope.launch {
                    val startTimeTrace = System.currentTimeMillis()
                    val timeModel = try {
                        TimeModel.Valid(Instant.parse(event.value))
                    } catch (e: DateTimeParseException) {
                        TimeModel.Invalid(
                            input = event.value,
                            parseErrorMessage = "Failed to parse Instant: ${event.value}".also {
                                Log.e(this::class.simpleName, it, e)
                            }
                        )
                    }
                    Log.d(this::class.simpleName, "Time handling took ${System.currentTimeMillis() - startTimeTrace} ms")

                    //
                    //
                    //
                    val startZoneTrace = System.currentTimeMillis()
                    var zoneErrorMessage: String? = null
                    val newZoneId = try {
                        ZoneId.of(event.zoneId)
                    } catch (e: Exception) {
                        zoneErrorMessage = "Failed to select ZoneId: ${event.zoneId}"
                        Log.e(this::class.simpleName, zoneErrorMessage, e)
                        _effect.emit(Effect.ZoneSelectionError(zoneErrorMessage))
                        null
                    }
                    Log.d(this::class.simpleName, "Zone handling took ${System.currentTimeMillis() - startZoneTrace} ms")

                    if(this.isActive) {
                        //update zone only on success
                        _state = if (newZoneId != null) {
                            _state.copy(
                                timeModel = timeModel,
                                zoneId = newZoneId,
                                setZoneAttemptErrorMessage = null,
                            )
                        } else {
                            _state.copy(
                                timeModel = timeModel,
                                setZoneAttemptErrorMessage = zoneErrorMessage,
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class Effect {

        data class ZoneSelectionError(
            val message: String
        ) : Effect()
    }

    sealed class Event {

        data class OnTimeChanged(
            val value: String,
            val zoneId: String?,
        ) : Event()
    }

    companion object {

        val TIME_MODEL_KEY: CreationExtras.Key<TimeComponentModel> = CreationExtras.Companion.Key()
    }
}