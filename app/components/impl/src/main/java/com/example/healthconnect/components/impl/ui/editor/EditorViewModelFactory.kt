package com.example.healthconnect.components.impl.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.impl.ui.editor.atomic.TimeFieldEditorViewModel
import com.example.healthconnect.components.impl.ui.editor.atomic.ValueFieldEditorViewModel
import kotlin.reflect.KClass

internal class EditorViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = when (modelClass) {
        TimeFieldEditorViewModel::class -> TimeFieldEditorViewModel(
            model = checkNotNull(extras[TimeFieldEditorViewModel.TIME_MODEL_KEY]),
        )

        ValueFieldEditorViewModel::class -> ValueFieldEditorViewModel(
            model = checkNotNull(extras[ValueFieldEditorViewModel.MODEL_KEY])
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}