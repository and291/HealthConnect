package com.example.healthconnect.utilty.impl.ui.screen.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.utilty.impl.domain.entity.Result
import com.example.healthconnect.utilty.impl.domain.usecase.Update
import com.example.healthconnect.utilty.impl.ui.screen.record.mapper.RecordMapper
import com.example.healthconnect.utilty.impl.ui.screen.record.model.BasalMetabolicRateEditorModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BasalMetabolicRateViewModel(
    initialRecord: BasalMetabolicRateRecord,
    private val recordMapper: RecordMapper,
    private val update: Update,
) : ViewModel() {

    private val initialModel =
        recordMapper.toUiModel(initialRecord) as BasalMetabolicRateEditorModel
    val isChanged: Boolean
        get() = initialModel != _state.basalMetabolicRateEditorModel

    private var _state: State by mutableStateOf(value = State.Edition(initialModel))
    val state: State
        get() = _state

    private var updateJob: Job? = null

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnMetaModelChanged -> (_state as? State.Edition)?.also {
                _state = State.Edition(
                    basalMetabolicRateEditorModel = it.basalMetabolicRateEditorModel.copy(
                        metadataEditorModel = event.metaModel
                    )
                )
            }

            is Event.OnTimeChanged -> (_state as? State.Edition)?.also {
                _state = State.Edition(
                    basalMetabolicRateEditorModel = it.basalMetabolicRateEditorModel.copy(
                        timeEditorModel = event.timeEditorModel
                    )
                )
            }

            is Event.OnPowerChanged -> (_state as? State.Edition)?.also {
                _state = State.Edition(
                    basalMetabolicRateEditorModel = it.basalMetabolicRateEditorModel.copy(
                        powerEditorModel = event.powerEditorModel
                    )
                )
            }

            is Event.OnSave -> (_state as? State.Edition)?.also { currentState ->
                updateJob?.takeIf { it.isActive }?.let {
                    Log.w(this::class.simpleName, "Attempt to start parallel updateJob was stopped")
                    return
                }

                if (!isChanged || !currentState.basalMetabolicRateEditorModel.isValid()) {
                    _state = State.Edition(
                        basalMetabolicRateEditorModel = currentState.basalMetabolicRateEditorModel,
                        errorCreatingEntity = "Invalid model or there is no changes to save"
                    )
                }

                if (event.upsert) {
                    TODO()
                } else {
                    val modifiedRecord = try {
                        recordMapper.toEntity(currentState.basalMetabolicRateEditorModel)
                    } catch (e: Exception) {
                        _state = State.Edition(
                            basalMetabolicRateEditorModel = currentState.basalMetabolicRateEditorModel,
                            errorCreatingEntity = "Error creating record: ${e.toString()}"
                        )
                        return
                    }

                    _state = State.UpdateInProgress(
                        basalMetabolicRateEditorModel = currentState.basalMetabolicRateEditorModel,
                        record = modifiedRecord,
                    )
                    updateJob = viewModelScope.launch {
                        _state = State.UpdateResult(
                            basalMetabolicRateEditorModel = currentState.basalMetabolicRateEditorModel,
                            result = update(modifiedRecord),
                        )
                    }
                }
            }
        }
    }

    sealed class State {

        abstract val basalMetabolicRateEditorModel: BasalMetabolicRateEditorModel

        /**
         * User able to modify values
         */
        data class Edition(
            override val basalMetabolicRateEditorModel: BasalMetabolicRateEditorModel,
            val errorCreatingEntity: String? = null,
            //validation and so on
        ) : State()

        /**
         * Show progress bar and stuff
         */
        data class UpdateInProgress(
            override val basalMetabolicRateEditorModel: BasalMetabolicRateEditorModel,
            val record: Record,
        ) : State()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class UpdateResult(
            override val basalMetabolicRateEditorModel: BasalMetabolicRateEditorModel,
            val result: Result //result of interaction with lib
        ) : State()
    }

    sealed class Event {

        data class OnTimeChanged(
            val timeEditorModel: TimeEditorModel,
        ) : Event()

        data class OnMetaModelChanged(
            val metaModel: MetadataEditorModel
        ) : Event()

        data class OnPowerChanged(
            val powerEditorModel: PowerEditorModel,
        ) : Event()

        data class OnSave(
            //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
            val upsert: Boolean = false
        ) : Event()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<BasalMetabolicRateRecord> = CreationExtras.Key()
    }
}
