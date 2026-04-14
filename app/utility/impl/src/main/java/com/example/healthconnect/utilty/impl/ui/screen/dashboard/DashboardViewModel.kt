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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

class DashboardViewModel(
    private val count: Count,
    private val nameMapper: RecordTypeNameMapper,
    private val iconMapper: RecordTypeIconMapper,
) : ViewModel() {

    private var collectJob: Job? = null
    private var consumeJob: Job? = null
    private val itemsCountChannel = Channel<Map<KClass<out Model>, Int?>>(Channel.CONFLATED)
    private val isRefreshingChannel = Channel<Boolean>(Channel.CONFLATED)

    private var _state by mutableStateOf<State.Data>(State.Data(buildEmptySegments()))
    val state: State get() = _state

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    init {
        startConsumeState()
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

            is Event.OnTypeClick -> viewModelScope.launch {
                _effect.emit(Effect.NavigateToRecords(event.recordType, event.nameRes))
            }

            Event.OnLibraryDataManagerClick -> viewModelScope.launch {
                _effect.emit(Effect.ShowLibraryDataManager)
            }
        }
    }

    private fun startRefreshData() {
        collectJob?.cancel()
        collectJob = viewModelScope.launch(Dispatchers.Default) {
            val mutableMap = SupportedModels.all.associateWith { null as Int? }.toMutableMap()
            isRefreshingChannel.send(true)

            SupportedModels.all.map { type -> count(type).map { type to it } }
                .merge()
                .collect { (type, result) ->
                    when (result) {
                        is FlowResult.Data -> {
                            mutableMap.replace(type, result.item)
                        }

                        is FlowResult.Terminal -> {
                            mutableMap.replace(type, null)
                        }
                    }
                    itemsCountChannel.send(mutableMap.toMap())
                }

            isRefreshingChannel.send(false)
        }
    }

    private fun startConsumeState() {
        if(consumeJob != null) {
            throw IllegalStateException("Unable to consume twice")
        }
        consumeJob = viewModelScope.launch(Dispatchers.Default) {
            val segments = buildEmptySegments()

            itemsCountChannel.consumeAsFlow().combine(
                flow = isRefreshingChannel.consumeAsFlow(),
                transform = { count, isRefreshing -> count to isRefreshing },
            ).collect { (map, isRefreshing) ->
                withContext(Dispatchers.Main) {
                    _state = State.Data(
                        segments = segments,
                        itemsCount = map,
                        isRefreshing = isRefreshing
                    )
                }
            }
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
                title = "Instantaneous",
                items = buildItems(SupportedModels.instantaneous)
            ),
            DashboardSegment(
                title = "Interval",
                items = buildItems(SupportedModels.interval)
            ),
            DashboardSegment(
                title = "Series",
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
