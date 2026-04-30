package com.example.healthconnect.permissions.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import com.example.healthconnect.permissions.impl.ui.screen.PermissionsViewModel
import kotlin.reflect.KClass

class PermissionsViewModelFactory(
    private val coordinator: PermissionCoordinator,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        PermissionsViewModel(coordinator) as T
}
