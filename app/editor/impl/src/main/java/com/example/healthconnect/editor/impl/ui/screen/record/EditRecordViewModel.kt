package com.example.healthconnect.editor.impl.ui.screen.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.api.ui.mapper.RecordMapper
import com.example.healthconnect.editor.api.ui.model.RecordModificationEvent
import com.example.healthconnect.editor.api.ui.model.RecordEditorModel
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel.State.*
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.api.domain.usecase.Update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditRecordViewModel(
    initialRecord: Record,
    private val recordMapper: RecordMapper,
    private val update: Update,
) : ViewModel() {

    private val initialModel = recordMapper.toUiModel(initialRecord)
    val isChanged: Boolean
        get() = initialModel != _state.editorModel

    private var _state: State<RecordEditorModel> by mutableStateOf(value = Edition(initialModel))
    val state: State<RecordEditorModel>
        get() = _state

    private var updateJob: Job? = null

    fun onEvent(event: RecordModificationEvent) {
        (_state as? Edition)?.also {
            _state = Edition(it.editorModel.update(event))
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdate -> (_state as? Edition)?.also { currentState ->
                updateJob?.takeIf { it.isActive }?.let {
                    Log.w(this::class.simpleName, "Attempt to start parallel updateJob was prevented")
                    return
                }

                if (!isChanged || !currentState.editorModel.isValid()) {
                    _state = Edition(
                        editorModel = currentState.editorModel,
                        errorCreatingEntity = "Invalid model or there is no changes to save"
                    )
                }

                if (event.upsert) {
                    TODO()
                } else {
                    val modifiedRecord = try {
                        recordMapper.toEntity(currentState.editorModel)
                    } catch (e: Exception) {
                        _state = Edition(
                            editorModel = currentState.editorModel,
                            errorCreatingEntity = "Error creating record: ${e.toString()}"
                        )
                        return
                    }

                    _state = UpdateInProgress(
                        editorModel = currentState.editorModel,
                        record = modifiedRecord,
                    )
                    updateJob = viewModelScope.launch {
                        _state = UpdateResult(
                            editorModel = currentState.editorModel,
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
        data class UpdateInProgress<T : RecordEditorModel>(
            override val editorModel: T,
            val record: Record,
        ) : State<T>()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class UpdateResult<T : RecordEditorModel>(
            override val editorModel: T,
            val result: Result //result of interaction with lib
        ) : State<T>()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<Record> = CreationExtras.Key()
    }
}