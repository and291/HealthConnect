package com.example.healthconnect.utilty.impl.ui.screen.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.Record
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.BodyTemperatureMeasurementLocationEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.editor.api.ui.model.BasalBodyTemperatureRecordEditorModel
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.editor.api.ui.mapper.RecordMapper
import com.example.healthconnect.utilty.api.domain.usecase.Update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BasalBodyTemperatureViewModel(
    initialRecord: BasalBodyTemperatureRecord,
    private val recordMapper: RecordMapper,
    private val update: Update,
) : ViewModel() {

    private val initialModel = recordMapper.toUiModel(initialRecord) as BasalBodyTemperatureRecordEditorModel
    val isChanged: Boolean
        get() = initialModel != _state.basalBodyTemperatureEditorModel

    private var _state: State by mutableStateOf(value = State.Edition(initialModel))
    val state: State
        get() = _state

    private var updateJob: Job? = null

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnMetaModelChanged -> (_state as? State.Edition)?.also {
                _state = State.Edition(
                    basalBodyTemperatureEditorModel = it.basalBodyTemperatureEditorModel.copy(
                        metadataEditorModel = event.metaModel
                    )
                )
            }

            is Event.OnMeasurementLocationSelected -> (_state as? State.Edition)?.also {
                _state = State.Edition(
                    basalBodyTemperatureEditorModel = it.basalBodyTemperatureEditorModel.copy(
                        measurementLocation = event.location //TODO check input
                    )
                )
            }

            is Event.OnTemperatureChanged -> (_state as? State.Edition)?.also {
                _state = State.Edition(
                    basalBodyTemperatureEditorModel = it.basalBodyTemperatureEditorModel.copy(
                        temperatureEditorModel = event.temperatureEditorModel
                    )
                )
            }

            is Event.OnTimeChanged -> (_state as? State.Edition)?.also {
                _state = State.Edition(
                    basalBodyTemperatureEditorModel = it.basalBodyTemperatureEditorModel.copy(
                        timeEditorModel = event.timeEditorModel
                    )
                )
            }

            is Event.OnSave -> (_state as? State.Edition)?.also { currentState ->
                updateJob?.takeIf { it.isActive }?.let {
                    Log.w(this::class.simpleName, "Attempt to start parallel updateJob was stopped")
                    return
                }

                if (!isChanged || !currentState.basalBodyTemperatureEditorModel.isValid()) {
                    _state = State.Edition(
                        basalBodyTemperatureEditorModel = currentState.basalBodyTemperatureEditorModel,
                        errorCreatingEntity = "Invalid model or there is no changes to save"
                    )
                }

                if (event.upsert) {
                    TODO()
                } else {
                    val modifiedRecord = try {
                        recordMapper.toEntity(currentState.basalBodyTemperatureEditorModel)
                    } catch (e: Exception) {
                        _state = State.Edition(
                            basalBodyTemperatureEditorModel = currentState.basalBodyTemperatureEditorModel,
                            errorCreatingEntity = "Error creating record: ${e.toString()}"
                        )
                        return
                    }

                    _state = State.UpdateInProgress(
                        basalBodyTemperatureEditorModel = currentState.basalBodyTemperatureEditorModel,
                        record = modifiedRecord,
                    )
                    updateJob = viewModelScope.launch {
                        _state = State.UpdateResult(
                            basalBodyTemperatureEditorModel = currentState.basalBodyTemperatureEditorModel,
                            result = update(modifiedRecord),
                        )
                    }
                }
            }
        }
    }

    sealed class State {

        abstract val basalBodyTemperatureEditorModel: BasalBodyTemperatureRecordEditorModel

        /**
         * User able to modify values
         */
        data class Edition(
            override val basalBodyTemperatureEditorModel: BasalBodyTemperatureRecordEditorModel,
            val errorCreatingEntity: String? = null,
            //validation and so on
        ) : State()

        /**
         * Show progress bar and stuff
         */
        data class UpdateInProgress(
            override val basalBodyTemperatureEditorModel: BasalBodyTemperatureRecordEditorModel,
            val record: Record,
        ) : State()

        /**
         * Display result of the update attempt
         * Allow to retry in case of failed attempt
         */
        data class UpdateResult(
            override val basalBodyTemperatureEditorModel: BasalBodyTemperatureRecordEditorModel,
            val result: Result //result of interaction with lib
        ) : State()
    }

    sealed class Event {

        data class OnTimeChanged(
            val timeEditorModel: TimeEditorModel,
        ) : Event()

        data class OnTemperatureChanged(
            val temperatureEditorModel: TemperatureEditorModel,
        ) : Event()

        data class OnMeasurementLocationSelected(
            val location: BodyTemperatureMeasurementLocationEditorModel
        ) : Event()

        data class OnMetaModelChanged(
            val metaModel: MetadataEditorModel
        ) : Event()

        data class OnSave(
            //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
            val upsert: Boolean = false
        ) : Event()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<BasalBodyTemperatureRecord> = CreationExtras.Key()
    }
}