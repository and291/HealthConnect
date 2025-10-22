package com.example.healthconnect.components.impl.ui.metadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.components.impl.ui.PowerEditorComponentViewModel
import com.example.healthconnect.components.impl.ui.TemperatureEditorComponentViewModel
import com.example.healthconnect.components.impl.ui.TimeEditorComponentViewModel
import kotlin.reflect.KClass

internal class ComponentViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T = when (modelClass) {
        MetadataEditorViewModel::class -> MetadataEditorViewModel(
            initialEntity = checkNotNull(extras[MetadataEditorViewModel.METADATA_ENTITY_KEY])
        )

        TimeEditorComponentViewModel::class -> TimeEditorComponentViewModel(
            timeEditorInternalModel = checkNotNull(extras[TimeEditorComponentViewModel.TIME_MODEL_KEY]),
        )

        TemperatureEditorComponentViewModel::class -> TemperatureEditorComponentViewModel(
            temperatureEditorModel = checkNotNull(extras[TemperatureEditorComponentViewModel.TEMPERATURE_MODEL_KEY])
        )

        PowerEditorComponentViewModel::class -> PowerEditorComponentViewModel(
            powerEditorModel = checkNotNull(extras[PowerEditorComponentViewModel.MODEL_KEY])
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}
