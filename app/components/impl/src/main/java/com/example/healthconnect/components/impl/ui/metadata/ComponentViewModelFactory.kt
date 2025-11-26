package com.example.healthconnect.components.impl.ui.metadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.impl.ui.ValueEditorComponentViewModel
import com.example.healthconnect.components.impl.ui.TimeEditorComponentViewModel
import kotlin.reflect.KClass

internal class ComponentViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = when (modelClass) {
        MetadataEditorViewModel::class -> MetadataEditorViewModel(
            initialModel = checkNotNull(extras[MetadataEditorViewModel.MODEL_KEY])
        )

        TimeEditorComponentViewModel::class -> TimeEditorComponentViewModel(
            model = checkNotNull(extras[TimeEditorComponentViewModel.TIME_MODEL_KEY]),
        )

        ValueEditorComponentViewModel::class -> ValueEditorComponentViewModel(
            model = checkNotNull(extras[ValueEditorComponentViewModel.MODEL_KEY])
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}
