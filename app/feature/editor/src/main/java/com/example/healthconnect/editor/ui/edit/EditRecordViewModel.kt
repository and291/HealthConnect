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
import com.example.healthconnect.editor.api.domain.entity.EditableField
import com.example.healthconnect.editor.api.domain.entity.Result
import com.example.healthconnect.editor.api.domain.usecase.GetEditable
import com.example.healthconnect.editor.api.domain.usecase.Update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.reflect.KClass

internal class EditRecordViewModel(
    private val editableUUID: UUID,
    private val kClass: KClass<*>,
    private val getEditable: GetEditable,
    private val update: Update,
) : ViewModel() {

    /**
     * The model as it was originally loaded, used to detect unsaved changes.
     * `null` until [getEditable] resolves.
     */
    private var initialModel: Editable? = null

    private var _state: State<Editable> by mutableStateOf(State.Loading)
    val state: State<Editable>
        get() = _state

    val isChanged: Boolean
        get() = initialModel?.let { initial ->
            (_state as? State.Loaded)?.model?.let { it != initial } ?: false
        } ?: false

    val sortedFields: List<EditableField>
        get() = (_state as? State.Loaded)?.model
            ?.getFields()
            ?.sortedBy { it.priority }
            .orEmpty()

    private var updateJob: Job? = null

    init {
        loadInitialModel()
    }

    private fun loadInitialModel() {
        _state = State.Loading
        viewModelScope.launch {
            //TODO handle errors
            val model = getEditable(editableUUID, kClass)
            initialModel = model
            _state = State.Edition(model)
        }
    }

    fun onEvent(event: EditEvent) {
        (_state as? State.Edition)?.also {
            _state = State.Edition(it.model.update(it.model, event))
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdate -> (_state as? State.Edition)?.also { currentState ->
                updateJob?.takeIf { it.isActive }?.let {
                    Log.w(this::class.simpleName, "Attempt to start parallel updateJob was prevented")
                    return
                }

                if (!isChanged || !currentState.model.isValid()) {
                    _state = State.Edition(
                        model = currentState.model,
                        errorCreatingEntity = "Invalid model or there is no changes to save"
                    )
                }

                if (event.upsert) {
                    TODO()
                } else {
                    _state = State.UpdateInProgress(
                        model = currentState.model,
                    )
                    updateJob = viewModelScope.launch {
                        _state = State.UpdateResult(
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

    sealed class State<out T : Editable> {

        /**
         * Resolving [initialModel] from the repository; the UI shows a progress indicator.
         */
        data object Loading : State<Nothing>()

        /**
         * The model has been resolved and is available for rendering/editing.
         */
        sealed class Loaded<T : Editable> : State<T>() {
            abstract val model: T
        }

        /**
         * User able to modify values
         */
        data class Edition<T : Editable>(
            override val model: T,
            val errorCreatingEntity: String? = null,
            //validation and so on
        ) : Loaded<T>()

        /**
         * Show progress bar and stuff
         */
        data class UpdateInProgress<T : Editable>(
            override val model: T,
        ) : Loaded<T>()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class UpdateResult<T : Editable>(
            override val model: T,
            val result: Result //result of interaction with lib
        ) : Loaded<T>()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<UUID> = CreationExtras.Key()
    }
}
