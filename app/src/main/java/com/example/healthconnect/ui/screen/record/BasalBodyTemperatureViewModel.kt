package com.example.healthconnect.ui.screen.record

import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.domain.model.Result
import com.example.healthconnect.domain.usecase.Update
import com.example.healthconnect.ui.screen.component.metadata.mapper.MetadataMapper
import com.example.healthconnect.ui.screen.component.metadata.model.MetadataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BasalBodyTemperatureViewModel(
    private val record: BasalBodyTemperatureRecord,
    private val metadataMapper: MetadataMapper,
    private val update: Update,
) : ViewModel() {

    private var metadataModel: MetadataModel =
        metadataMapper.toUiModel(record.metadata)
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
                metadataModel = event.metaModel
            }

            Event.OnSave -> viewModelScope.launch {
                if (false) { //TODO is ClientRecordId used to read the data??
                    //upsert here
                    //https://developer.android.com/health-and-fitness/guides/health-connect/develop/write-data
                } else {
                    val modifiedRecord = BasalBodyTemperatureRecord(
                        time = record.time,
                        zoneOffset = record.zoneOffset,
                        temperature = record.temperature,
                        measurementLocation = record.measurementLocation,
                        metadata = metadataMapper.toMetadata(metadataModel)
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
        }
    }

    sealed class Effect {

        data object RecordUpdated : Effect()
    }

    sealed class Event {

        data class OnMetaModelChanged(
            val metaModel: MetadataModel
        ) : Event()

        data object OnSave : Event()
    }

    companion object {

        val RECORD_KEY: CreationExtras.Key<BasalBodyTemperatureRecord> = CreationExtras.Key()
    }
}