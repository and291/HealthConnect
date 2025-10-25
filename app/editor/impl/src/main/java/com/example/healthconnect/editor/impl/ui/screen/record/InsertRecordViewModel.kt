package com.example.healthconnect.editor.impl.ui.screen.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.api.ui.editor.Editor
import com.example.healthconnect.editor.api.ui.editor.EditorFactory
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import com.example.healthconnect.editor.api.ui.model.RecordEditorModel
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class InsertRecordViewModel(
    recordClass: KClass<Record>,
    editorFactory: EditorFactory,
    private val metadataMapper: MetadataMapper,
    private val insert: Insert,
) : ViewModel() {

    private val editor: Editor<Record, RecordEditorModel> = editorFactory.create(recordClass)

    private var _state: State<RecordEditorModel> by mutableStateOf(
        State.Edition(
            editorModel = editor.toModel(
                record = editor.createDefault(),
                metadataMapper = metadataMapper,
            )
        )
    )
    val state: State<RecordEditorModel>
        get() = _state

    private var insertJob: Job? = null

    fun onEvent(event: RecordModificationEvent) {
        (_state as? State.Edition<RecordEditorModel>)?.also {
            _state = State.Edition(editor.update(it.editorModel, event))
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

                if (!currentState.editorModel.isValid()) {
                    _state = State.Edition(
                        editorModel = currentState.editorModel,
                        errorCreatingEntity = "Invalid model"
                    )
                }

                val modifiedRecord = try {
                    editor.toRecord(currentState.editorModel, metadataMapper)
                } catch (e: Exception) {
                    _state = State.Edition(
                        editorModel = currentState.editorModel,
                        errorCreatingEntity = "Error creating record: ${e.toString()}"
                    )
                    return
                }

                _state = State.InsertInProgress(
                    editorModel = currentState.editorModel,
                    record = modifiedRecord,
                )
                insertJob = viewModelScope.launch {
                    _state = State.InsertResult(
                        editorModel = currentState.editorModel,
                        result = insert(modifiedRecord),
                    )
                }
            }
        }
    }

    sealed class Event {

        data object OnInsert : Event()
    }

    sealed class State<T : RecordEditorModel> {

        abstract val editorModel: T

        /**
         * User able to modify values
         */
        data class Edition<T : RecordEditorModel>(
            override val editorModel: T,
            val errorCreatingEntity: String? = null,
            //validation and so on
        ) : State<T>()

        /**
         * Show progress bar and stuff
         */
        data class InsertInProgress<T : RecordEditorModel>(
            override val editorModel: T,
            val record: Record,
        ) : State<T>()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class InsertResult<T : RecordEditorModel>(
            override val editorModel: T,
            val result: Result, //result of interaction with lib
        ) : State<T>()
    }

    companion object {

        val RECORD_CLASS_KEY: CreationExtras.Key<KClass<Record>> = CreationExtras.Key()
    }
}