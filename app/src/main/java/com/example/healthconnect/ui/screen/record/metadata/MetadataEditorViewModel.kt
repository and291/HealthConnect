package com.example.healthconnect.ui.screen.record.metadata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import java.time.Instant

class MetadataEditorViewModel(
    initialMetadataModel: MetadataModel
) : ViewModel() {

    private var _state by mutableStateOf(initialMetadataModel)

    val state: MetadataModel
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            is Event.OnRecordingMethodSelected -> {
                _state.copy(recordingMethod = event.value)
            }

            is Event.OnClientRecordIdChanged -> {
                _state.copy(clientRecordId = event.value)
            }

            is Event.OnClientVersionChanged -> {
                //TODO check input
                _state.copy(clientRecordVersion = event.value.toLong())
            }
        }
    }

    sealed class Event {

        data class OnRecordingMethodSelected(
            val value: Int,
        ) : Event()

        data class OnClientRecordIdChanged(
            val value: String
        ) : Event()

        data class OnClientVersionChanged(
            val value: String
        ) : Event()
    }

    data class MetadataModel(
        val recordingMethod: Int,
        val id: String = "",
        val dataOriginPackageName: String = "",
        val lastModifiedTime: Instant = Instant.EPOCH,
        val clientRecordId: String = "",
        val clientRecordVersion: Long = 0,

        val deviceType: Int? = null,
        val deviceManufacturer: String? = null,
        val deviceModel: String? = null,
    )

    companion object {
        val METADATA_MODEL_KEY: CreationExtras.Key<MetadataModel> = CreationExtras.Key()
    }
}

