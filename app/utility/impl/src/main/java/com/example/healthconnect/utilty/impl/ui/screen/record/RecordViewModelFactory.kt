package com.example.healthconnect.utilty.impl.ui.screen.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.utilty.impl.ui.mapper.MetadataMapper
import com.example.healthconnect.utilty.impl.domain.usecase.Update
import kotlin.reflect.KClass

class RecordViewModelFactory(
    private val metadataMapper: MetadataMapper,
    private val update: Update,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T = when (modelClass) {
        BasalBodyTemperatureViewModel::class -> BasalBodyTemperatureViewModel(
            record = checkNotNull(extras[BasalBodyTemperatureViewModel.RECORD_KEY]),
            metadataMapper = metadataMapper,
            update = update,
        )

        else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.simpleName)
    } as T
}