package com.example.healthconnect.components.impl.ui.metadata

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.records.metadata.Device
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.domain.entity.metadata.DeviceEntity
import com.example.healthconnect.components.api.domain.entity.metadata.MetadataEntity

class MetadataEditorViewModel(
    initialEntity: MetadataEntity
) : ViewModel() {

    private var _state by mutableStateOf(initialEntity)

    val state: MetadataEntity
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
                deviceEntity = DeviceEntity.Specified(
                    type = Device.Companion.TYPE_UNKNOWN,
                    manufacturer = "",
                    model = ""
                )
            )

            Event.OnRemoveDevice -> _state.copy(
                deviceEntity = DeviceEntity.Empty
            )

            is Event.OnTypeSelected -> _state.copy(
                deviceEntity = (_state.deviceEntity as DeviceEntity.Specified).copy(
                    type = event.type
                ) as DeviceEntity
            )

            is Event.OnManufacturerChanged -> _state.copy(
                deviceEntity = (_state.deviceEntity as DeviceEntity.Specified).copy(
                    manufacturer = event.manufacturer
                ) as DeviceEntity
            )

            is Event.OnModelChanged -> _state.copy(
                deviceEntity = (_state.deviceEntity as DeviceEntity.Specified).copy(
                    model = event.model
                ) as DeviceEntity
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

        val METADATA_ENTITY_KEY: CreationExtras.Key<MetadataEntity> = CreationExtras.Companion.Key()
    }
}