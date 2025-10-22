package com.example.healthconnect.utilty.impl.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.utilty.api.domain.entity.Payload
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.Read
import com.example.healthconnect.utilty.impl.ui.model.DisplayRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class RecordsViewModel(
    private val recordType: KClass<out Record>,
    private val read: Read,
    private val delete: Delete,
) : ViewModel() {

    private var _state by mutableStateOf<State>(State.Loading)
    val state: State
        get() = _state

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

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
                val result = read(recordType).also {
                    Log.d(this::class.qualifiedName, "Records list refreshed with result: $it")
                }
                when (result) {
                    is Result.IoException -> TODO()
                    is Result.IpcFailure -> TODO()
                    is Result.PermissionRequired -> {
                        _effect.emit(Effect.RequestSinglePermission(result.sdkPermission))
                    }

                    is Result.Success -> {
                        when (val payload = result.payload) {
                            is Payload.InsertList -> TODO()
                            is Payload.ReadList<*> -> {
                                _state = State.Data(payload.list.map {
                                    DisplayRecord(
                                        description = it.toString(),
                                        metadataId = it.metadata.id,
                                        record = it,
                                    )
                                })
                            }

                            is Payload.Removed -> TODO()
                            Payload.Updated -> TODO()
                        }
                    }

                    is Result.UnhandledException -> TODO()
                    is Result.UnpermittedAccess -> TODO()
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
            val records: List<DisplayRecord>
        ) : State()
    }

    sealed class Effect {

        data class OpenRecordScreen(
            val record: Record,
        ) : Effect()

        data class RequestSinglePermission(
            val sdkPermission: String
        ) : Effect()
    }

    sealed class Event {

        data class OnRecordClick(
            val record: Record,
        ) : Event()

        data class DeleteRecord(
            val recordType: KClass<out Record>,
            val metadataId: String,
        ) : Event()

        data object Refresh : Event()
    }

    companion object {
        val RECORD_TYPE_KEY: CreationExtras.Key<KClass<out Record>> = CreationExtras.Key()
    }
}
