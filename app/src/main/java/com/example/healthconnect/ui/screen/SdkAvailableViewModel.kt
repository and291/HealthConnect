package com.example.healthconnect.ui.screen

import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.domain.model.Result
import com.example.healthconnect.domain.usecase.Insert
import com.example.healthconnect.ui.screen.SdkAvailableViewModel.Effect.RequestSinglePermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset

class SdkAvailableViewModel(
    private val insert: Insert,
) : ViewModel() {

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect: StateFlow<Effect?> = _effect.asStateFlow()

    fun effectConsumed(effect: Effect) {
        if (_effect.value == effect) {
            _effect.value = null
        }
    }

    fun insertSteps() = viewModelScope.launch {
        val stepsRecord = StepsRecord(
            count = 120,
            startTime = Instant.now().minusSeconds(120),
            endTime = Instant.now(),
            startZoneOffset = ZoneOffset.UTC,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
        )
        val result = insert(stepsRecord)

        val effect = when (result) {
            is Result.IoException -> TODO()
            is Result.IpcFailure -> TODO()
            is Result.PermissionRequired -> RequestSinglePermission(result.sdkPermission)
            is Result.Success -> Effect.StepsInsertedSuccessfully(result.string)
            is Result.UnhandledException -> TODO()
            is Result.UnpermittedAccess -> TODO()
        }

        _effect.emit(effect)
    }

    sealed class Effect {

        data class RequestSinglePermission(
            val sdkPermission: String
        ) : Effect()

        data class StepsInsertedSuccessfully(
            val result: String
        ) : Effect()
    }
}
