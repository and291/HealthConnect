package com.example.healthconnect.components.impl.ui.editor.atomic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField

//TODO pass a Record's valid range and validate input inside the component?
// keep in mind: some Records rely on platform's instance validation inside init{}
internal class ValueFieldEditorViewModel(
    private val model: ValueField,
) : ViewModel() {

    private var _state by mutableStateOf(model)

    val state: ValueField
        get() = _state

    fun onEvent(event: Event) {
        _state = when (event) {
            is Event.OnValueChanged -> when (model.type.valueType) {
                ValueField.Type.ValueType.Double -> ValueField.Dbl(
                    value = event.text,
                    type = model.type,
                    instanceId = model.instanceId,
                )
                ValueField.Type.ValueType.Long -> ValueField.Lng(
                    value = event.text,
                    type = model.type,
                    instanceId = model.instanceId,
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

        val MODEL_KEY: CreationExtras.Key<ValueField> = CreationExtras.Companion.Key()
    }
}