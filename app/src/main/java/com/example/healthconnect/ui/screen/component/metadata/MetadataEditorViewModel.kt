package com.example.healthconnect.ui.screen.component.metadata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.metadata.Device
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.ui.screen.component.metadata.model.DeviceModel
import com.example.healthconnect.ui.screen.component.metadata.model.MetadataModel

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

            Event.OnSpecifyDevice -> _state.copy(
                deviceModel = DeviceModel.Specified(
                    type = Device.Companion.TYPE_UNKNOWN,
                    manufacturer = "",
                    model = ""
                )
            )

            Event.OnRemoveDevice -> _state.copy(
                deviceModel = DeviceModel.Empty
            )

            is Event.OnTypeSelected -> _state.copy(
                deviceModel = (_state.deviceModel as DeviceModel.Specified).copy(
                    type = event.type
                ) as DeviceModel
            )

            is Event.OnManufacturerChanged -> _state.copy(
                deviceModel = (_state.deviceModel as DeviceModel.Specified).copy(
                    manufacturer = event.manufacturer
                ) as DeviceModel
            )

            is Event.OnModelChanged -> _state.copy(
                deviceModel = (_state.deviceModel as DeviceModel.Specified).copy(
                    model = event.model
                ) as DeviceModel
            )
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

        data object OnSpecifyDevice : Event()

        data object OnRemoveDevice : Event()

        data class OnTypeSelected(
            val type: Int,
        ) : Event()

        data class OnManufacturerChanged(
            val manufacturer: String
        ) : Event()

        data class OnModelChanged(
            val model: String
        ) : Event()
    }

    companion object {

        val METADATA_MODEL_KEY: CreationExtras.Key<MetadataModel> = CreationExtras.Companion.Key()
    }
}