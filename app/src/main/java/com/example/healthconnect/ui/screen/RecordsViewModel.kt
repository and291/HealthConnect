package com.example.healthconnect.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.domain.model.Payload
import com.example.healthconnect.domain.model.Result
import com.example.healthconnect.domain.usecase.Read
import com.example.healthconnect.ui.screen.RecordsViewModel.Effect.RequestSinglePermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class RecordsViewModel(
    private val recordType: KClass<Record>,
    private val read: Read,
) : ViewModel() {

    private var _state by mutableStateOf<State>(State.Loading)
    private val _effect = MutableStateFlow<Effect?>(null)

    val state: State
        get() = _state
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    init {
        viewModelScope.launch {
            when (val result = read(recordType)) {
                is Result.IoException -> TODO()
                is Result.IpcFailure -> TODO()
                is Result.PermissionRequired -> {
                    _effect.emit(RequestSinglePermission(result.sdkPermission))
                }

                is Result.Success -> {
                    when (result.payload) {
                        is Payload.InsertList -> TODO()
                        is Payload.ReadList<*> -> {
                            _state = State.Data(result.payload.list.map { it.toString() })
                        }
                    }
                }

                is Result.UnhandledException -> TODO()
                is Result.UnpermittedAccess -> TODO()
            }
        }
    }

    sealed class State {

        data object Loading : State()

        data class Data(
            val records: List<String>
        ) : State()
    }

    sealed class Effect {

        data class RequestSinglePermission(
            val sdkPermission: String
        ) : Effect()
    }

    companion object {
        val RECORD_TYPE_KEY: CreationExtras.Key<KClass<Record>> = CreationExtras.Key()
    }
}
