package com.example.healthconnect.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthconnect.domain.LibraryRepository

//TODO change naming as current is not actually correct: this single VM requires constructor parameter
class ParameterlessViewModelFactory(
    private val libraryRepository: LibraryRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ActivityViewModel::class.java -> ActivityViewModel(libraryRepository)
            else -> throw IllegalStateException("Unknown ViewModel class:" + modelClass.canonicalName)
        } as T
    }
}