package com.example.healthconnect.dashboard.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.dashboard.api.domain.entity.CountResult
import com.example.healthconnect.dashboard.api.domain.usecase.CountRecords
import com.example.healthconnect.dashboard.api.domain.usecase.GetDashboardCatalog
import com.example.healthconnect.dashboard.ui.screen.dashboard.model.DashboardItem
import com.example.healthconnect.dashboard.ui.screen.dashboard.model.DashboardItem.LoadingState
import com.example.healthconnect.dashboard.ui.screen.dashboard.model.DashboardSegment
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
    getCatalog: GetDashboardCatalog,
    private val countRecords: CountRecords,
) : ViewModel() {

    private val catalog = getCatalog()

    private var collectJob: Job? = null
    private val itemsCountStateFlow = MutableStateFlow<Map<KClass<*>, LoadingState>>(emptyMap())
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

            Event.OnPermissionsClick -> {
                _effect.trySend(Effect.NavigateToPermissions)
            }
        }
    }

    private fun startRefreshData() {
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            val allTypes = catalog.flatMap { it.types }
            val mutableMap = allTypes.associate { it.type to (LoadingState.InProgress as LoadingState) }.toMutableMap()
            isRefreshingStateFlow.emit(true)

            allTypes
                .map { descriptor ->
                    countRecords(descriptor.type).map { descriptor.type to it.toLoadingState() }
                }
                .merge()
                .collect { (type, loadingState) ->
                    mutableMap.replace(type, loadingState)
                    itemsCountStateFlow.emit(mutableMap.toMap())
                }

            isRefreshingStateFlow.emit(false)
        }
    }

    private fun CountResult.toLoadingState(): LoadingState = when (this) {
        is CountResult.Counted -> LoadingState.Counted(count)
        is CountResult.Failed -> LoadingState.Failed(errorIcon = errorIcon)
    }

    private fun buildSegments(
        map: Map<KClass<*>, LoadingState>
    ): List<DashboardSegment> = catalog.map { category ->
        DashboardSegment(
            title = category.titleRes,
            items = category.types.map { descriptor ->
                DashboardItem(
                    recordType = descriptor.type,
                    nameRes = descriptor.nameRes,
                    icon = descriptor.icon,
                    state = map.getOrDefault(descriptor.type, LoadingState.InProgress),
                )
            }
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
            val recordType: KClass<*>,
            @param:StringRes val nameRes: Int,
        ) : Effect()

        object ShowLibraryDataManager : Effect()
        object NavigateToPermissions : Effect()
    }

    sealed class Event {
        data object Refresh : Event()
        data class OnTypeClick(
            val recordType: KClass<*>,
            @param:StringRes val nameRes: Int,
        ) : Event()

        data object OnLibraryDataManagerClick : Event()
        data object OnPermissionsClick : Event()
    }
}
