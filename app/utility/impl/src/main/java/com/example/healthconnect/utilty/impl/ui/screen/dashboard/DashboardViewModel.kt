package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.utilty.api.domain.entity.Payload
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.impl.domain.SupportedRecords
import com.example.healthconnect.utilty.impl.domain.usecase.Read
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.model.DashboardItem
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.model.DashboardSegment
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class DashboardViewModel(
    private val read: Read,
    private val nameMapper: RecordTypeNameMapper,
    private val iconMapper: RecordTypeIconMapper,
) : ViewModel() {

    private var _state by mutableStateOf<State>(State.Loading)
    val state: State get() = _state

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.Refresh -> viewModelScope.launch {
                _state = State.Loading
                val counts = loadCounts()
                _state = State.Data(buildSegments(counts))
            }

            is Event.OnTypeClick -> viewModelScope.launch {
                _effect.emit(Effect.NavigateToRecords(event.recordType, event.nameRes))
            }

            Event.OnLibraryDataManagerClick -> viewModelScope.launch {
                _effect.emit(Effect.ShowLibraryDataManager)
            }
        }
    }

    private suspend fun loadCounts(): Map<KClass<*>, Int?> {
        val allTypes = SupportedRecords.instantaneous + SupportedRecords.interval + SupportedRecords.series
        return coroutineScope {
            allTypes.map { type ->
                @Suppress("UNCHECKED_CAST")
                async { type to readCount(type as KClass<out Record>) }
            }.awaitAll().toMap()
        }
    }

    private suspend fun readCount(type: KClass<out Record>): Int? {
        return when (val result = read(type)) {
            is Result.Success -> when (val payload = result.payload) {
                is Payload.ReadList<*> -> payload.list.size
                else -> null
            }
            else -> null
        }
    }

    private fun buildSegments(counts: Map<KClass<*>, Int?>): List<DashboardSegment> {
        fun buildItems(types: List<KClass<*>>): List<DashboardItem> = types.map { type ->
            @Suppress("UNCHECKED_CAST")
            DashboardItem(
                recordType = type as KClass<out Record>,
                nameRes = nameMapper.nameRes(type),
                icon = iconMapper.icon(type),
                count = counts[type],
            )
        }

        return listOf(
            DashboardSegment(title = "Instantaneous", items = buildItems(SupportedRecords.instantaneous)),
            DashboardSegment(title = "Interval", items = buildItems(SupportedRecords.interval)),
            DashboardSegment(title = "Series", items = buildItems(SupportedRecords.series)),
        )
    }

    sealed class State {
        data object Loading : State()
        data class Data(val segments: List<DashboardSegment>) : State()
    }

    sealed class Effect {
        data class NavigateToRecords(
            val recordType: KClass<out Record>,
            @param:StringRes val nameRes: Int,
        ) : Effect()
        object ShowLibraryDataManager : Effect()
    }

    sealed class Event {
        data object Refresh : Event()
        data class OnTypeClick(
            val recordType: KClass<out Record>,
            @param:StringRes val nameRes: Int,
        ) : Event()
        data object OnLibraryDataManagerClick : Event()
    }
}
