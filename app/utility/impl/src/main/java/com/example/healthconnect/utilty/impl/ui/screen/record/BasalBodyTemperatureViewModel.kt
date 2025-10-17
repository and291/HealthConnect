package com.example.healthconnect.utilty.impl.ui.screen.record

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.units.Temperature
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.data.mapper.MetadataMapper
import com.example.healthconnect.components.api.ui.model.MetadataModel
import com.example.healthconnect.components.api.ui.model.InstantModel
import com.example.healthconnect.components.api.ui.model.TemperatureModel
import com.example.healthconnect.utilty.impl.domain.entity.Result
import com.example.healthconnect.utilty.impl.domain.usecase.Update
import com.example.healthconnect.utilty.impl.ui.screen.record.model.BasalBodyTemperatureModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BasalBodyTemperatureViewModel(
    record: BasalBodyTemperatureModel,
    private val metadataMapper: MetadataMapper,
    private val update: Update,
) : ViewModel() {

    private var _state by mutableStateOf(record)

    val state: BasalBodyTemperatureModel
        get() = _state

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
                    metadataModel = event.metaModel
                )
            }

            Event.OnSave -> viewModelScope.launch {
                if (false) { //TODO is ClientRecordId used to read the data??
                    //upsert here
                    //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
                } else {
                    val instantModel = _state.instantModel as InstantModel.Valid
                    val temperatureModel = _state.temperatureModel as TemperatureModel.Valid
                    if (!_state.metadataModel.isValid()) { return@launch }
                    val modifiedRecord = BasalBodyTemperatureRecord(
                        time = instantModel.instant,
                        zoneOffset = instantModel.zoneOffset,
                        temperature = Temperature.celsius(temperatureModel.temperatureCelsius),
                        measurementLocation = _state.measurementLocation,
                        metadata = metadataMapper.toLibMetadata(_state.metadataModel)
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
                    temperatureModel = event.temperatureModel
                )
            }

            is Event.OnTimeChanged -> {
                _state = _state.copy(
                    instantModel = event.instantModel
                )
            }
        }
    }

    sealed class Effect {

        data object RecordUpdated : Effect()
    }

    sealed class Event {

        data class OnTimeChanged(
            val instantModel: InstantModel,
        ): Event()

        data class OnTemperatureChanged(
            val temperatureModel: TemperatureModel,
        ) : Event()

        data class OnMeasurementLocationSelected(
            val location: Int
        ): Event()

        data class OnMetaModelChanged(
            val metaModel: MetadataModel
        ) : Event()

        data object OnSave : Event()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<BasalBodyTemperatureModel> = CreationExtras.Key()
    }
}