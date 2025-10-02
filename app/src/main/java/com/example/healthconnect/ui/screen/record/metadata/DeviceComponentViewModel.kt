package com.example.healthconnect.ui.screen.record.metadata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.metadata.Device
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras

class DeviceComponentViewModel(
    initialState: DeviceModel
) : ViewModel() {

    private var _state by mutableStateOf(initialState)

    val deviceModel: DeviceModel
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            Event.OnSpecifyDevice -> DeviceModel.Specified(
                type = Device.TYPE_UNKNOWN,
                manufacturer = "",
                model = ""
            )

            Event.OnRemoveDevice -> DeviceModel.Empty
        }
    }

    sealed class Event {

        data object OnSpecifyDevice : Event()

        data object OnRemoveDevice : Event()
    }

    sealed class DeviceModel {

        data object Empty : DeviceModel()

        data class Specified(
            val type: Int,
            val manufacturer: String,
            val model: String,
        ) : DeviceModel()
    }

    companion object {

        val DEVICE_KEY: CreationExtras.Key<DeviceModel> = CreationExtras.Key()
    }
}