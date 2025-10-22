package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel.Invalid
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel.Valid

//TODO pass a Record's valid temperature range and validate input inside the component?
internal class TemperatureEditorComponentViewModel(
    temperatureEditorModel: TemperatureEditorModel
) : ViewModel() {

    private var _state by mutableStateOf(temperatureEditorModel)

    val state: TemperatureEditorModel
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            is Event.OnTemperatureChanged -> try {
                Valid(
                    parsedValue = event.text.toDouble(),
                    value = event.text,
                )
            } catch (e: Exception) {
                Log.d(this::class.simpleName, "Failed to parse Double celsius: ${event.text}", e)
                Invalid(event.text)
            }
        }
    }

    sealed class Event {

        data class OnTemperatureChanged(
            val text: String
        ) : Event()
    }

    companion object {

        val TEMPERATURE_MODEL_KEY: CreationExtras.Key<TemperatureEditorModel> =
            CreationExtras.Companion.Key()
    }
}