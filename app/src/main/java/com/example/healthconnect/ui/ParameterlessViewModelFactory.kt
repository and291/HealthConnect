package com.example.healthconnect.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.ui.screen.SdkPermissionsViewModel
import com.example.healthconnect.ui.screen.SdkAvailableViewModel
import com.example.healthconnect.ui.screen.SdkUpdateRequiredViewModel

//TODO change naming as current is not actually correct: this single VM requires constructor parameter
class ParameterlessViewModelFactory(
    private val applicationContext: Context,
    private val libraryRepository: LibraryRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ActivityViewModel::class.java -> ActivityViewModel(libraryRepository)
            SdkPermissionsViewModel::class.java -> SdkPermissionsViewModel(libraryRepository)
            SdkAvailableViewModel::class.java -> SdkAvailableViewModel()
            SdkUpdateRequiredViewModel::class.java -> SdkUpdateRequiredViewModel(applicationContext)
            else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.canonicalName)
        } as T
    }
}