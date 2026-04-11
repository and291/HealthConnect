package com.example.healthconnect.editor.impl.ui.screen.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.impl.ui.editor.record.Editor
import com.example.healthconnect.editor.impl.ui.editor.EditorFactory
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.domain.model.FieldModificationEvent
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class InsertRecordViewModel(
    recordClass: KClass<out Model>,
    editorFactory: EditorFactory,
    metadataMapper: MetadataMapper,
    private val insert: Insert,
) : ViewModel() {

    private val editor: Editor<Record, Model> = editorFactory.createByModel(recordClass)

    private var _state: State<Model> by mutableStateOf(
        State.Edition(
            model = editor.toModel(
                record = editor.createDefault(),
                mapper = metadataMapper,
            )
        )
    )
    val state: State<Model>
        get() = _state

    val sortedFields
        get() = _state.model.getFields().sortedBy { it.priority }

    private var insertJob: Job? = null

    fun onEvent(event: FieldModificationEvent) {
        (_state as? State.Edition<Model>)?.also {
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

    sealed class State<T : Model> {

        abstract val model: T

        /**
         * User able to modify values
         */
        data class Edition<T : Model>(
            override val model: T,
            val errorCreatingEntity: String? = null,
            //validation and so on
        ) : State<T>()

        /**
         * Show progress bar and stuff
         */
        data class InsertInProgress<T : Model>(
            override val model: T,
        ) : State<T>()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class InsertResult<T : Model>(
            override val model: T,
            val result: Result, //result of interaction with lib
        ) : State<T>()
    }

    companion object {

        val RECORD_CLASS_KEY: CreationExtras.Key<KClass<out Model>> = CreationExtras.Key()
    }
}