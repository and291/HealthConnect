package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.R
import com.example.healthconnect.utilty.impl.domain.SupportedModels
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.model.DashboardItem
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.model.DashboardItem.LoadingState
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.model.DashboardSegment
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class DashboardViewModel(
    private val count: Count,
    private val nameMapper: RecordTypeNameMapper,
    private val iconMapper: RecordTypeIconMapper,
) : ViewModel() {

    private var collectJob: Job? = null
    private val itemsCountStateFlow = MutableStateFlow<Map<KClass<out Model>, LoadingState>>(emptyMap())
    private val isRefreshingStateFlow = MutableStateFlow(false)

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    val state: StateFlow<State> = itemsCountStateFlow
        .combine(isRefreshingStateFlow) { map, isRefreshing ->
            State.Data(
                segments = buildSegments(map),
                isRefreshing = isRefreshing
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Data(
                segments = buildSegments(emptyMap()),
                isRefreshing = false,
            )
        )


    init {
        startRefreshData()
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.Refresh -> startRefreshData()

            is Event.OnTypeClick -> {
                _effect.trySend(Effect.NavigateToRecords(event.recordType, event.nameRes))
            }

            Event.OnLibraryDataManagerClick -> {
                _effect.trySend(Effect.ShowLibraryDataManager)
            }
        }
    }

    private fun startRefreshData() {
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            val mutableMap = SupportedModels.all.associateWith { LoadingState.InProgress as LoadingState }.toMutableMap()
            isRefreshingStateFlow.emit(true)

            SupportedModels.all
                .map { type ->
                    count(type).map { type to LoadingState.Loaded(it) }
                }
                .merge()
                .collect { (type, loadingState) ->
                    mutableMap.replace(type, loadingState)
                    itemsCountStateFlow.emit(mutableMap.toMap())
                }

            isRefreshingStateFlow.emit(false)
        }
    }

    private fun buildSegments(
        map: Map<KClass<out Model>, LoadingState>
    ): List<DashboardSegment> {
        fun buildItems(
            types: List<KClass<out Model>>,
        ): List<DashboardItem> = types.map { type ->
            DashboardItem(
                recordType = type,
                nameRes = nameMapper.nameRes(type),
                icon = iconMapper.icon(type),
                state = map.getOrDefault(type, LoadingState.InProgress)
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
