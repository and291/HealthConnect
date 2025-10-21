package com.example.healthconnect.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.domain.LibraryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SdkPermissionsViewModel(
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    var isLoading: Boolean by mutableStateOf(false)
    var grantedPermissions: List<String> by mutableStateOf(emptyList())

    private var requestGrantedPermissionsJob: Job? = null

    fun update() {
        requestGrantedPermissionsJob?.takeIf { it.isActive }?.let {
            return
        }
        requestGrantedPermissionsJob = viewModelScope.launch {
            isLoading = true
//            delay(5000)
            grantedPermissions = libraryRepository.getGrantedPermissions().map {
                it.removePrefix("android.permission.health.")
            }
            isLoading = false
        }
    }
}
