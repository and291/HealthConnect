package com.example.healthconnect.ui.screen.component

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

class TimeComponentViewModel(
    time: Instant,
    zoneOffset: ZoneOffset? = null,
) : ViewModel() {

    private var _state by mutableStateOf(time to zoneOffset)

    val state: Pair<Instant, ZoneOffset?>
        get() = _state

    fun onEvent(event: Event) = when (event) {
        is Event.OnTimeValueChanged -> try {
            val instant = Instant.parse(event.value)
            _state = _state.copy(first = instant)
        } catch (e: Exception) {
            Log.e(this::class.simpleName, "failed to parse Instant from: ${event.value}", e)
            //TODO throw some effect
        }

        is Event.OnZoneSelected -> try {
            val zoneOffset = ZoneId.of(event.zoneId).rules.getOffset(_state.first)
            _state = _state.copy(second = zoneOffset)
        } catch (e: Exception) {
            Log.e(this::class.simpleName, "failed to parse ZoneId from: ${event.zoneId}", e)
            //TODO throw some effect
        }
    }

    sealed class Event {

        data class OnTimeValueChanged(
            val value: String
        ) : Event()

        data class OnZoneSelected(
            val zoneId: String
        ) : Event()
    }

    companion object {

        val INSTANT_KEY: CreationExtras.Key<Instant> = CreationExtras.Companion.Key()
        val ZONE_OFFSET_KEY: CreationExtras.Key<ZoneOffset?> = CreationExtras.Companion.Key()
    }
}