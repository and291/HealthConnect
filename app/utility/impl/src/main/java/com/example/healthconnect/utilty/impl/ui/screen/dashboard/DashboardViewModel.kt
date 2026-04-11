package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.SupportedModels
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
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
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class DashboardViewModel(
    private val count: Count,
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

    private suspend fun loadCounts(): Map<KClass<out Model>, Int?> {
        return coroutineScope {
            SupportedModels.all.map { type ->
                async { type to readCount(type) }
            }.awaitAll().toMap()
        }
    }

    private suspend fun readCount(
        type: KClass<out Model>
    ): Int? = count(type).fold(null as Int?) { acc, result ->
        when (result) {
            is FlowResult.Data -> (acc ?: 0) + result.item
            is FlowResult.Terminal -> null // Treat any terminal error as failure
        }
    }

    private fun buildSegments(counts: Map<KClass<out Model>, Int?>): List<DashboardSegment> {
        fun buildItems(types: List<KClass<out Model>>): List<DashboardItem> = types.map { type ->
            DashboardItem(
                recordType = type,
                nameRes = nameMapper.nameRes(type),
                icon = iconMapper.icon(type),
                count = counts[type],
            )
        }

        return listOf(
            DashboardSegment(title = "Instantaneous", items = buildItems(SupportedModels.instantaneous)),
            DashboardSegment(title = "Interval", items = buildItems(SupportedModels.interval)),
            DashboardSegment(title = "Series", items = buildItems(SupportedModels.series)),
        )
    }

    sealed class State {
        data object Loading : State()
        data class Data(val segments: List<DashboardSegment>) : State()
    }

    sealed class Effect {
        data class NavigateToRecords(
            val recordType: KClass<out Model>,
            @param:StringRes val nameRes: Int,
        ) : Effect()
        object ShowLibraryDataManager : Effect()
    }

    sealed class Event {
        data object Refresh : Event()
        data class OnTypeClick(
            val recordType: KClass<out Model>,
            @param:StringRes val nameRes: Int,
        ) : Event()
        data object OnLibraryDataManagerClick : Event()
    }
}
