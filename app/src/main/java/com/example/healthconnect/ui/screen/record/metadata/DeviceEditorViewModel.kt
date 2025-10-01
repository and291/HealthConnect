package com.example.healthconnect.ui.screen.record.metadata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.metadata.Device
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras

class DeviceEditorViewModel(
    initialDeviceModel: DeviceModel
) : ViewModel() {

    private var _state by mutableStateOf(initialDeviceModel)

    val deviceModel: DeviceModel
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            is Event.OnTypeSelected -> {
                _state.copy(type = event.type)
            }
            is Event.OnManufacturerChanged -> {
                _state.copy(manufacturer = event.manufacturer)
            }
            is Event.OnModelChanged -> {
                _state.copy(model = event.model)
            }
        }
    }

    sealed class Event {

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

    data class DeviceModel(
        val type: Int = Device.TYPE_UNKNOWN,
        val manufacturer: String = "",
        val model: String = "",
    )

    companion object {
        val DEVICE_KEY: CreationExtras.Key<DeviceModel> = CreationExtras.Key()
    }
}