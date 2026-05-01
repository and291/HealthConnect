package com.example.healthconnect.utilty.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.ReadAll
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.Companion.RECORD_TYPE_KEY
import kotlin.reflect.KClass

class RecordsViewModelFactory(
    private val readAll: ReadAll,
    private val delete: Delete,
    private val coordinator: PermissionCoordinator,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when (modelClass) {
            RecordsViewModel::class -> RecordsViewModel(
                readAll = readAll,
                delete = delete,
                recordType = checkNotNull(extras[RECORD_TYPE_KEY]),
                coordinator = coordinator,
            )
            else -> throw IllegalStateException("Unknown ViewModel class: ${modelClass.simpleName}")
        } as T
    }
}
