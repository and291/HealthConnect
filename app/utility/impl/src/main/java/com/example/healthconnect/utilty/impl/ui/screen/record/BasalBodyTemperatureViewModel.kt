package com.example.healthconnect.utilty.impl.ui.screen.record

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.units.Temperature
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.utilty.impl.domain.entity.Result
import com.example.healthconnect.utilty.impl.domain.usecase.Update
import com.example.healthconnect.utilty.impl.ui.mapper.MetadataMapper
import com.example.healthconnect.utilty.impl.ui.screen.record.mapper.RecordMapper
import com.example.healthconnect.utilty.impl.ui.screen.record.model.BasalBodyTemperatureModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BasalBodyTemperatureViewModel(
    recordMapper: RecordMapper,
    initialRecord: BasalBodyTemperatureRecord ,
    private val metadataMapper: MetadataMapper,
    private val update: Update,
) : ViewModel() {

    private val initialModel = recordMapper.toUiModel(initialRecord)
    private var _state by mutableStateOf(initialModel)

    val state: BasalBodyTemperatureModel
        get() = _state
    val isChanged: Boolean
        get() = initialModel != _state

    private val _effect = MutableStateFlow<Effect?>(null)

    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnMetaModelChanged -> {
                _state = _state.copy(
                    metadataEditorModel = event.metaModel
                )
            }

            Event.OnSave -> viewModelScope.launch {
                if (!isChanged) {
                    return@launch
                }

                if (false) { //TODO is ClientRecordId used to read the data??
                    //upsert here
                    //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
                } else {
                    val timeEditorModel = _state.timeEditorModel as TimeEditorModel.Valid
                    val temperatureEditorModel = _state.temperatureEditorModel as TemperatureEditorModel.Valid
                    if (!_state.metadataEditorModel.isValid()) {
                        return@launch
                    }
                    val modifiedRecord = BasalBodyTemperatureRecord(
                        time = timeEditorModel.instant,
                        zoneOffset = timeEditorModel.zoneOffset,
                        temperature = Temperature.celsius(temperatureEditorModel.temperatureCelsius),
                        measurementLocation = _state.measurementLocation,
                        metadata = metadataMapper.toLibMetadata(_state.metadataEditorModel)
                    )
                    when (update(modifiedRecord)) {
                        is Result.IoException -> TODO()
                        is Result.IpcFailure -> TODO()
                        is Result.PermissionRequired -> TODO()
                        is Result.Success -> {
                            _effect.emit(Effect.RecordUpdated)
                        }

                        is Result.UnhandledException -> TODO()
                        is Result.UnpermittedAccess -> TODO()
                    }
                }
            }

            is Event.OnMeasurementLocationSelected -> {
                _state = _state.copy(
                    measurementLocation = event.location //TODO check input
                )
            }

            is Event.OnTemperatureChanged -> {
                _state = _state.copy(
                    temperatureEditorModel = event.temperatureEditorModel
                )
            }

            is Event.OnTimeChanged -> {
                _state = _state.copy(
                    timeEditorModel = event.timeEditorModel
                )
            }
        }
    }

    sealed class Effect {

        data object RecordUpdated : Effect()
    }

    sealed class Event {

        data class OnTimeChanged(
            val timeEditorModel: TimeEditorModel,
        ) : Event()

        data class OnTemperatureChanged(
            val temperatureEditorModel: TemperatureEditorModel,
        ) : Event()

        data class OnMeasurementLocationSelected(
            val location: Int
        ) : Event()

        data class OnMetaModelChanged(
            val metaModel: MetadataEditorModel
        ) : Event()

        data object OnSave : Event()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<BasalBodyTemperatureRecord> = CreationExtras.Key()
    }
}