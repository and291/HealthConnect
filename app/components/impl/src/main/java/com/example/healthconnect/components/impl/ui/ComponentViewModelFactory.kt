package com.example.healthconnect.components.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

internal class ComponentViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = when (modelClass) {
        TimeEditorComponentViewModel::class -> TimeEditorComponentViewModel(
            model = checkNotNull(extras[TimeEditorComponentViewModel.TIME_MODEL_KEY]),
        )

        ValueEditorComponentViewModel::class -> ValueEditorComponentViewModel(
            model = checkNotNull(extras[ValueEditorComponentViewModel.MODEL_KEY])
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}