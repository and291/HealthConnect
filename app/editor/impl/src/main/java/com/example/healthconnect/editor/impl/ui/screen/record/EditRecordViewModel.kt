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
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel.State.*
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.api.domain.usecase.Update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditRecordViewModel(
    initialRecord: Record,
    editorFactory: EditorFactory,
    private val metadataMapper: MetadataMapper,
    private val update: Update,
) : ViewModel() {

    private val editor: Editor<Record, Model> = editorFactory.create(initialRecord::class)
    private val initialModel = editor.toModel(initialRecord, metadataMapper)
    val isChanged: Boolean
        get() = initialModel != _state.model

    private var _state: State<Model> by mutableStateOf(value = Edition(initialModel))
    val state: State<Model>
        get() = _state

    val sortedComponents
        get() = _state.model.getFields().sortedBy { it.priority }

    private var updateJob: Job? = null

    fun onEvent(event: FieldModificationEvent) {
        (_state as? Edition)?.also {
            _state = Edition(editor.update(it.model, event))
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdate -> (_state as? Edition)?.also { currentState ->
                updateJob?.takeIf { it.isActive }?.let {
                    Log.w(this::class.simpleName, "Attempt to start parallel updateJob was prevented")
                    return
                }

                if (!isChanged || !currentState.model.isValid()) {
                    _state = Edition(
                        model = currentState.model,
                        errorCreatingEntity = "Invalid model or there is no changes to save"
                    )
                }

                if (event.upsert) {
                    TODO()
                } else {
                    val modifiedRecord = try {
                        editor.toRecord(currentState.model, metadataMapper)
                    } catch (e: Exception) {
                        _state = Edition(
                            model = currentState.model,
                            errorCreatingEntity = "Error creating record: ${e.toString()}"
                        )
                        return
                    }

                    _state = UpdateInProgress(
                        model = currentState.model,
                        record = modifiedRecord,
                    )
                    updateJob = viewModelScope.launch {
                        _state = UpdateResult(
                            model = currentState.model,
                            result = update(modifiedRecord),
                        )
                    }
                }
            }
        }
    }

    sealed class Event {

        data class OnUpdate(
            //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
            val upsert: Boolean = false
        ) : Event()
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
        data class UpdateInProgress<T : Model>(
            override val model: T,
            val record: Record,
        ) : State<T>()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class UpdateResult<T : Model>(
            override val model: T,
            val result: Result //result of interaction with lib
        ) : State<T>()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<Record> = CreationExtras.Key()
    }
}