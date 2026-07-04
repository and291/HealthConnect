package com.example.healthconnect.editor.ui.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.api.domain.entity.EditEvent
import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.editor.api.domain.entity.Result
import com.example.healthconnect.editor.api.domain.usecase.CreateEditable
import com.example.healthconnect.editor.api.domain.usecase.Insert
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

internal class InsertRecordViewModel(
    recordClass: KClass<*>,
    createEditable: CreateEditable,
    private val insert: Insert,
) : ViewModel() {

    private val editor: Editable = createEditable(recordClass)

    private var _state: State<Editable> by mutableStateOf(State.Edition(editor))
    val state: State<Editable>
        get() = _state

    val sortedFields
        get() = _state.model.getFields().sortedBy { it.priority }

    private var insertJob: Job? = null

    fun onEvent(event: EditEvent) {
        (_state as? State.Edition<Editable>)?.also {
            _state = State.Edition(editor.update(it.model, event))
        }
    }

    fun onEvent(event: Event) {
        when (event) {

            is Event.OnInsert -> (_state as? State.Edition)?.also { currentState ->
                insertJob?.takeIf { it.isActive }?.let {
                    Log.w(
                        this::class.simpleName,
                        "Attempt to start parallel insertJob was prevented"
                    )
                    return
                }

                if (!currentState.model.isValid()) {
                    _state = State.Edition(
                        model = currentState.model,
                        errorCreatingEntity = "Invalid model"
                    )
                }

                _state = State.InsertInProgress(
                    model = currentState.model,
                )
                insertJob = viewModelScope.launch {
                    _state = State.InsertResult(
                        model = currentState.model,
                        result = insert(currentState.model),
                    )
                }
            }
        }
    }

    sealed class Event {

        data object OnInsert : Event()
    }

    sealed class State<T : Editable> {

        abstract val model: T

        /**
         * User able to modify values
         */
        data class Edition<T : Editable>(
            override val model: T,
            val errorCreatingEntity: String? = null,
            //validation and so on
        ) : State<T>()

        /**
         * Show progress bar and stuff
         */
        data class InsertInProgress<T : Editable>(
            override val model: T,
        ) : State<T>()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class InsertResult<T : Editable>(
            override val model: T,
            val result: Result, //result of interaction with lib
        ) : State<T>()
    }

    companion object {

        val RECORD_CLASS_KEY: CreationExtras.Key<KClass<*>> = CreationExtras.Key()
    }
}