package com.example.healthconnect.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.healthconnect.domain.LibraryRecords.instantaneous
import com.example.healthconnect.domain.LibraryRecords.interval
import com.example.healthconnect.domain.LibraryRecords.series
import kotlin.reflect.KClass

class SdkAvailableViewModel(
) : ViewModel() {

    private val _state by mutableStateOf(State.RecordTypes())

    val state: State
        get() = _state

    sealed class State {

        data class RecordTypes(
            val availableTypes: List<KClass<*>> = instantaneous + interval + series
        ) : State()
    }
}
