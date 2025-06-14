package com.example.healthconnect.ui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.domain.LibraryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SdkPermissionsViewModel(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    var isLoading by mutableStateOf<Boolean>(false)
    var grantedPermissions by mutableStateOf<Set<String>>(emptySet())

    init {
        updateGrantedPermissionsSet()
    }

    fun updateGrantedPermissionsSet() {
        //TODO wait for the first request to finish before starting each next request (use Flow?)
        isLoading = true
        viewModelScope.launch {
            grantedPermissions = libraryRepository.getGrantedPermissions()
            delay(5000)
        }
        isLoading = false
    }
}
