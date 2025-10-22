package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.PowerEditorModel

internal class PowerEditorComponentViewModel(
    powerEditorModel: PowerEditorModel
) : ViewModel() {

    private var _state by mutableStateOf(powerEditorModel)

    val state: PowerEditorModel
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            is Event.OnPowerChanged -> try {
                PowerEditorModel.Valid(
                    parsedValue = event.text.toDouble(),
                    value = event.text
                )
            } catch (e: Exception) {
                Log.d(
                    this::class.simpleName,
                    "Failed to parse Double kilocalories per day: ${event.text}",
                    e
                )
                PowerEditorModel.Invalid(event.text)
            }
        }
    }

    sealed class Event {

        data class OnPowerChanged(
            val text: String
        ) : Event()
    }

    companion object {

        val MODEL_KEY: CreationExtras.Key<PowerEditorModel> = CreationExtras.Companion.Key()
    }
}
