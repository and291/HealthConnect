package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.annotation.StringRes
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.healthconnect.utilty.impl.R
import kotlin.reflect.KClass

class DashboardViewModel(
    private val count: Count,
    private val nameMapper: RecordTypeNameMapper,
    private val iconMapper: RecordTypeIconMapper,
) : ViewModel() {

    private var collectJob: Job? = null
    private val itemsCountStateFlow = MutableStateFlow<Map<KClass<out Model>, Int?>>(emptyMap())
    private val isRefreshingStateFlow = MutableStateFlow(false)

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    private val segments = buildEmptySegments()
    val state: StateFlow<State> = itemsCountStateFlow
        .combine(isRefreshingStateFlow) { map, isRefreshing ->
            State.Data(
                segments = segments,
                itemsCount = map,
                isRefreshing = isRefreshing
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Data(
                segments = segments,
                itemsCount = emptyMap(),
                isRefreshing = false,
            )
        )


    init {
        startRefreshData()
    }

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.Refresh -> startRefreshData()

            is Event.OnTypeClick -> {
                _effect.value = Effect.NavigateToRecords(event.recordType, event.nameRes)
            }

            Event.OnLibraryDataManagerClick -> {
                _effect.value = Effect.ShowLibraryDataManager
            }
        }
    }

    private fun startRefreshData() {
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            val mutableMap = SupportedModels.all.associateWith { null as Int? }.toMutableMap()
            isRefreshingStateFlow.emit(true)

            SupportedModels.all.map { type -> countRecords(type) }
                .merge()
                .collect { (type, value) ->
                    mutableMap.replace(type, value)
                    itemsCountStateFlow.emit(mutableMap.toMap())
                }

            isRefreshingStateFlow.emit(false)
        }
    }

    private fun countRecords(
        type: KClass<out Model>,
    ): Flow<Pair<KClass<out Model>, Int?>> = count(type).map {
        when (it) {
            is FlowResult.Data -> type to it.item
            is FlowResult.Terminal -> type to null
        }
    }

    private fun buildEmptySegments(): List<DashboardSegment> {
        fun buildItems(
            types: List<KClass<out Model>>,
        ): List<DashboardItem> = types.map { type ->
            DashboardItem(
                recordType = type,
                nameRes = nameMapper.nameRes(type),
                icon = iconMapper.icon(type),
            )
        }

        return listOf(
            DashboardSegment(
                title = R.string.dashboard_segment_instantaneous,
                items = buildItems(SupportedModels.instantaneous)
            ),
            DashboardSegment(
                title = R.string.dashboard_segment_interval,
                items = buildItems(SupportedModels.interval)
            ),
            DashboardSegment(
                title = R.string.dashboard_segment_series,
                items = buildItems(SupportedModels.series)
            ),
        )
    }

    sealed class State {
        data class Data(
            val segments: List<DashboardSegment>,
            val itemsCount: Map<KClass<out Model>, Int?> = emptyMap(),
            val isRefreshing: Boolean = false,
        ) : State()
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
