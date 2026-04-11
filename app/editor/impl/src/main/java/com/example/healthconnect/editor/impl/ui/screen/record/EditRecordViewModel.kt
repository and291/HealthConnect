package com.example.healthconnect.editor.impl.ui.screen.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.editor.api.domain.model.FieldModificationEvent
import com.example.healthconnect.editor.impl.ui.editor.EditorFactory
import com.example.healthconnect.editor.impl.ui.editor.record.Editor
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel.State.Edition
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel.State.UpdateInProgress
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel.State.UpdateResult
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.api.domain.usecase.Update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditRecordViewModel(
    private val initialModel: Model,
    editorFactory: EditorFactory,
    private val update: Update,
) : ViewModel() {

    private val editor: Editor<Record, Model> = editorFactory.createByModel(initialModel::class)
    val isChanged: Boolean
        get() = initialModel != _state.model

    private var _state: State<Model> by mutableStateOf(value = Edition(initialModel))
    val state: State<Model>
        get() = _state

    val sortedFields
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
                    _state = UpdateInProgress(
                        model = currentState.model,
                    )
                    updateJob = viewModelScope.launch {
                        _state = UpdateResult(
                            model = currentState.model,
                            result = update(currentState.model),
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

        val RECORD_KEY: CreationExtras.Key<Model> = CreationExtras.Key()
    }
}