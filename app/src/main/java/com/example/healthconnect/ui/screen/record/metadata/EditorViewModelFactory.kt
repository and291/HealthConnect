package com.example.healthconnect.ui.screen.record.metadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

class EditorViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when (modelClass) {
            DeviceEditorViewModel::class -> DeviceEditorViewModel(
                initialState = checkNotNull(extras[DeviceEditorViewModel.DEVICE_KEY])
            )
            else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
        } as T
    }
}
