package com.example.healthconnect.permissions.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.permissions.api.usecase.LibraryPermissionResolver
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import com.example.healthconnect.permissions.impl.ui.screen.PermissionsViewModel
import com.example.healthconnect.utilty.api.ui.mapper.RecordTypeNameMapper
import kotlin.reflect.KClass

class PermissionsViewModelFactory(
    private val coordinator: PermissionCoordinator,
    private val allModelTypes: List<KClass<out Model>>,
    private val recordTypeNameMapper: RecordTypeNameMapper,
    private val permissionResolver: LibraryPermissionResolver,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return when (modelClass) {
            PermissionsViewModel::class -> {
                PermissionsViewModel(
                    coordinator,
                    allModelTypes,
                    recordTypeNameMapper,
                    permissionResolver
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }
    }
}
