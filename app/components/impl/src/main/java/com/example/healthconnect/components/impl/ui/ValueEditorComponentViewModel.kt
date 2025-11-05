package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.ui.model.ValueComponentModel

//TODO pass a Record's valid range and validate input inside the component?
// keep in mind: some Records rely on platform's instance validation inside init{}
internal class ValueEditorComponentViewModel(
    private val editorModel: ValueComponentModel,
) : ViewModel() {

    private var _state by mutableStateOf(editorModel)

    val state: ValueComponentModel
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            is Event.OnValueChanged -> try {
                when (editorModel.type.valueType) {
                    ValueComponentModel.Type.ValueType.Double -> ValueComponentModel.ValidDouble(
                        parsedValue = event.text.toDouble(),
                        value = event.text,
                        type = editorModel.type,
                    )
                    ValueComponentModel.Type.ValueType.Long -> ValueComponentModel.ValidLong(
                        parsedValue = event.text.toLong(),
                        value = event.text,
                        type = editorModel.type,
                    )
                }

            } catch (e: Exception) {
                Log.d(
                    this::class.simpleName,
                    "Failed to parse Double ${editorModel.type.suffix}: ${event.text}",
                    e
                )
                ValueComponentModel.Invalid(
                    value = event.text,
                    type = editorModel.type,
                )
            }
        }
    }

    sealed class Event {

        data class OnValueChanged(
            val text: String,
        ) : Event()
    }

    companion object {

        val MODEL_KEY: CreationExtras.Key<ValueComponentModel> = CreationExtras.Companion.Key()
    }
}