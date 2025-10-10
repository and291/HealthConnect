package com.example.healthconnect.ui.screen.component.metadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.ui.screen.component.TimeComponentViewModel
import kotlin.reflect.KClass

class ComponentViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T = when (modelClass) {
        MetadataEditorViewModel::class -> MetadataEditorViewModel(
            initialMetadataModel = checkNotNull(extras[MetadataEditorViewModel.METADATA_MODEL_KEY])
        )

        TimeComponentViewModel::class -> TimeComponentViewModel(
            time = checkNotNull(extras[TimeComponentViewModel.INSTANT_KEY]),
            zoneOffset = extras[TimeComponentViewModel.ZONE_OFFSET_KEY],
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}
