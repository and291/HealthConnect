package com.example.healthconnect.utilty.impl.ui.screen.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.domain.usecase.ReadAll
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.Effect.*
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.State.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class RecordsViewModel(
    private val recordType: KClass<out Model>,
    private val readAll: ReadAll,
    private val delete: Delete,
) : ViewModel() {

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    private var collectItemsJob: Job? = null
    private val _refreshSateFlow = MutableStateFlow(false)
    private val _itemsStateFlow = MutableStateFlow<List<Model>>(emptyList())
    private val _nextPageTriggerChannel = Channel<Unit>(Channel.UNLIMITED)

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    init {
        onEvent(Event.Refresh)
    }

    val stateFlow: StateFlow<State> = _itemsStateFlow
        .combine(_refreshSateFlow) { items, isRefreshing -> Data(items, isRefreshing) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Data(emptyList(), true)
        )

    fun onEvent(event: Event) {
        when (event) {

            is Event.DeleteRecord -> viewModelScope.launch {
                delete(recordType = event.recordType, metadataId = event.metadataId)
                //just send event to update content. Will fix later
                onEvent(Event.Refresh)
            }

            is Event.OnRecordClick -> viewModelScope.launch {
                _effect.emit(OpenRecordScreen(event.record))
            }

            Event.Refresh -> {
                refresh()
            }

            Event.NextPage -> {
                _nextPageTriggerChannel.trySend(Unit)
            }
        }
    }

    private fun refresh() {
        collectItemsJob?.cancel()
        @Suppress("ControlFlowWithEmptyBody")
        while (_nextPageTriggerChannel.tryReceive().isSuccess) { } // drain stale signals

        collectItemsJob = viewModelScope.launch {
            _refreshSateFlow.emit(true)
            _itemsStateFlow.emit(emptyList())

            readAll(recordType)
                .map { page ->
                    when (page) {
                        is FlowResult.Data -> Data(page.item.items, false)
                        is FlowResult.Terminal -> TODO()
                    }
                }
                .runningFold(emptyList<Model>(), { acc, models -> acc + models.records })
                .drop(1)
                .collect {
                    _refreshSateFlow.emit(false)
                    _nextPageTriggerChannel.receive()
                    _itemsStateFlow.emit(it)
                }
        }
    }

    sealed class State {

        data class Data(
            val records: List<Model>,
            val isRefreshing: Boolean,
        ) : State()
    }

    sealed class Effect {

        data class OpenRecordScreen(
            val record: Model,
        ) : Effect()

        data class RequestSinglePermission(
            val sdkPermission: String,
        ) : Effect()
    }

    sealed class Event {

        data class OnRecordClick(
            val record: Model,
        ) : Event()

        data class DeleteRecord(
            val recordType: KClass<out Model>,
            val metadataId: String,
        ) : Event()

        data object Refresh : Event()

        data object NextPage : Event()
    }

    companion object {
        val RECORD_TYPE_KEY: CreationExtras.Key<KClass<out Model>> = CreationExtras.Key()
    }
}