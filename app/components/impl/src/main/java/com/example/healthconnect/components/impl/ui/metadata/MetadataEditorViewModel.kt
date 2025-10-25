package com.example.healthconnect.components.impl.ui.metadata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.metadata.Device
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.DeviceComponentModel
import com.example.healthconnect.components.api.ui.model.MetadataComponentModel

internal class MetadataEditorViewModel(
    initialModel: MetadataComponentModel
) : ViewModel() {

    private var _state by mutableStateOf(initialModel)

    val state: MetadataComponentModel
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
                _state.copy(clientRecordVersion = event.value)
            }

            Event.OnSpecifyDevice -> _state.copy(
                deviceComponentModel = DeviceComponentModel.Specified(
                    type = Device.Companion.TYPE_UNKNOWN,
                    manufacturer = "",
                    model = ""
                )
            )

            Event.OnRemoveDevice -> _state.copy(
                deviceComponentModel = DeviceComponentModel.Empty
            )

            is Event.OnTypeSelected -> _state.copy(
                deviceComponentModel = (_state.deviceComponentModel as DeviceComponentModel.Specified).copy(
                    type = event.type
                ) as DeviceComponentModel
            )

            is Event.OnManufacturerChanged -> _state.copy(
                deviceComponentModel = (_state.deviceComponentModel as DeviceComponentModel.Specified).copy(
                    manufacturer = event.manufacturer
                ) as DeviceComponentModel
            )

            is Event.OnModelChanged -> _state.copy(
                deviceComponentModel = (_state.deviceComponentModel as DeviceComponentModel.Specified).copy(
                    model = event.model
                ) as DeviceComponentModel
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

        val MODEL_KEY: CreationExtras.Key<MetadataComponentModel> = CreationExtras.Companion.Key()
    }
}