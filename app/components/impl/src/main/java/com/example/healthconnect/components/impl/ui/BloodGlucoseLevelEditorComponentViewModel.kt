package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.BloodGlucoseLevelEditorModel

internal class BloodGlucoseLevelEditorComponentViewModel(
    editorModel: BloodGlucoseLevelEditorModel
) : ViewModel() {

    private var _state by mutableStateOf(editorModel)

    val state: BloodGlucoseLevelEditorModel
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            is Event.OnLevelChanged -> try {
                BloodGlucoseLevelEditorModel.Valid(
                    parsedValue = event.text.toDouble(),
                    value = event.text
                )
            } catch (e: Exception) {
                Log.d(
                    this::class.simpleName,
                    "Failed to parse Double millimoles per liter: ${event.text}",
                    e
                )
                BloodGlucoseLevelEditorModel.Invalid(event.text)
            }
        }
    }

    sealed class Event {

        data class OnLevelChanged(
            val text: String
        ) : Event()
    }

    companion object {

        val MODEL_KEY: CreationExtras.Key<BloodGlucoseLevelEditorModel> = CreationExtras.Companion.Key()
    }
}

