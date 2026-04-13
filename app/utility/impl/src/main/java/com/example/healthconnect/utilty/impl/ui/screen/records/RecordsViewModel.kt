package com.example.healthconnect.utilty.impl.ui.screen.records

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.domain.usecase.ReadPages
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class RecordsViewModel(
    private val recordType: KClass<out Model>,
    private val pages: ReadPages,
    private val delete: Delete,
) : ViewModel() {

    private var _state by mutableStateOf<State>(State.Loading)
    val state: State
        get() = _state

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    init {
        Log.d("Pages", "initiating first refresh")
        onEvent(Event.Refresh)
    }

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.DeleteRecord -> viewModelScope.launch {
                delete(recordType = event.recordType, metadataId = event.metadataId)
                //just send event to update content. Will fix later
                onEvent(Event.Refresh)
            }

            Event.Refresh -> viewModelScope.launch {
                val pagesChannel = pages(recordType)
                Log.d("Pages", "displaying first page")
                when (val i = pagesChannel.receive()) {
                    is FlowResult.Data<Page<Model>> -> {
                        _state = State.Data(
                            records = i.item.items,
                            channel = pagesChannel,
                        )
                    }

                    is FlowResult.Terminal -> TODO()
                }
            }

            Event.NextPage -> {
                (_state as? State.Data)?.let { localState ->
                    viewModelScope.launch {
                        Log.d("Pages", "displaying next page")
                        when (val i = localState.channel.receive()) {
                            is FlowResult.Data<Page<Model>> -> {
                                _state = localState.copy(
                                    records = localState.records + i.item.items,
                                )
                            }

                            is FlowResult.Terminal -> TODO()
                        }
                    }
                }
            }

            is Event.OnRecordClick -> viewModelScope.launch {
                _effect.emit(Effect.OpenRecordScreen(event.record))
            }
        }
    }

    sealed class State {

        data object Loading : State()

        data class Data(
            val records: List<Model>,
            val channel: Channel<FlowResult<Page<Model>>>,
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