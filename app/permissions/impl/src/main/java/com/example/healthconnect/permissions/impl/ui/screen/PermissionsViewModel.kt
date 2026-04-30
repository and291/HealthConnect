package com.example.healthconnect.permissions.impl.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionRequest
import com.example.healthconnect.permissions.api.domain.PermissionStatus
import com.example.healthconnect.permissions.api.domain.PermissionType
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PermissionsViewModel(
    private val coordinator: PermissionCoordinator,
) : ViewModel() {

    private val isLoading = MutableStateFlow(true)
    private val statuses = MutableStateFlow<List<PermissionStatus>>(emptyList())

    val state: StateFlow<State> = combine(isLoading, statuses) { loading, statusList ->
        State(
            isLoading = loading,
            readPermissions = statusList.filter { it.permission.type == PermissionType.Read },
            writePermissions = statusList.filter { it.permission.type == PermissionType.Write },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = State(isLoading = true, emptyList(), emptyList()),
    )

    init {
        onEvent(Event.Refresh)
    }

    fun onEvent(event: Event) {
        when (event) {
            Event.Refresh -> loadStatuses()
            is Event.RequestPermission -> requestPermissions(PermissionRequest.Single(event.permission))
            Event.RequestAllMissing -> {
                val missing = statuses.value
                    .filter { !it.isGranted }
                    .map { it.permission }
                    .toSet()
                if (missing.isNotEmpty()) {
                    requestPermissions(PermissionRequest.Multiple(missing))
                }
            }
        }
    }

    private fun loadStatuses() {
        viewModelScope.launch {
            isLoading.emit(true)
            statuses.emit(coordinator.getPermissionStatuses())
            isLoading.emit(false)
        }
    }

    private fun requestPermissions(request: PermissionRequest) {
        viewModelScope.launch {
            if (coordinator.pendingRequest.value != null) return@launch
            coordinator.request(request)
            coordinator.results.first()
            loadStatuses()
        }
    }

    data class State(
        val isLoading: Boolean,
        val readPermissions: List<PermissionStatus>,
        val writePermissions: List<PermissionStatus>,
    ) {
        val hasAnyDenied: Boolean
            get() = (readPermissions + writePermissions).any { !it.isGranted }
    }

    sealed class Event {
        data object Refresh : Event()
        data class RequestPermission(val permission: HealthPermission) : Event()
        data object RequestAllMissing : Event()
    }
}
