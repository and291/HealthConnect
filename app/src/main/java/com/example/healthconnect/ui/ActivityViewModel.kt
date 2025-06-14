package com.example.healthconnect.ui

import androidx.compose.runtime.mutableIntStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import com.example.healthconnect.domain.LibraryRepository

class ActivityViewModel(
    libraryRepository: LibraryRepository
) : ViewModel() {

    //TODO consider splitting SDK statuses from BL-statuses of App (think twice before start messing with that!)
    @HealthConnectClient.Companion.AvailabilityStatus
    val sdkStatus = mutableIntStateOf(HealthConnectClient.SDK_UNAVAILABLE)

    init {
        sdkStatus.intValue = libraryRepository.getSdkStatus()
    }
}
