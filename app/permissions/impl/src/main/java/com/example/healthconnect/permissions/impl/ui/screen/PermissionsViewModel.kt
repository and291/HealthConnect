package com.example.healthconnect.permissions.impl.ui.screen

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.permissions.api.domain.framework.HealthPermission
import com.example.healthconnect.permissions.api.domain.framework.PermissionRequest
import com.example.healthconnect.permissions.api.domain.entity.PermissionStatus
import com.example.healthconnect.permissions.api.domain.framework.usecase.LibraryPermissionResolver
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionCoordinator
import com.example.healthconnect.utilty.api.ui.mapper.RecordTypeNameMapper
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PermissionsViewModel(
    private val coordinator: PermissionCoordinator,
    allModelTypes: List<KClass<out Model>>,
    recordTypeNameMapper: RecordTypeNameMapper,
    permissionResolver: LibraryPermissionResolver,
) : ViewModel() {

    // Both read and write for the same record type share the data-type label.
    private val permissionNames: Map<String, Int> = allModelTypes.flatMap { modelClass ->
        val nameRes = recordTypeNameMapper.nameRes(modelClass)
        listOf(
            permissionResolver.readPermission(modelClass).permissionString to nameRes,
            permissionResolver.writePermission(modelClass).permissionString to nameRes,
        )
    }.toMap()

    private val readPermissionStrings: Set<String> = allModelTypes
        .map { permissionResolver.readPermission(it).permissionString }
        .toSet()

    private val writePermissionStrings: Set<String> = allModelTypes
        .map { permissionResolver.writePermission(it).permissionString }
        .toSet()

    private val isLoading = MutableStateFlow(true)
    private val statuses = MutableStateFlow<List<PermissionStatus>>(emptyList())

    val state: StateFlow<State> = combine(isLoading, statuses) { loading, statusList ->
        State(
            isLoading = loading,
            readPermissions = statusList
                .filter { it.permission.permissionString in readPermissionStrings }
                .map { it.toUiItem() },
            writePermissions = statusList
                .filter { it.permission.permissionString in writePermissionStrings }
                .map { it.toUiItem() },
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

    private fun PermissionStatus.toUiItem() =
        PermissionUiItem(this, requireNotNull(permissionNames[permission.permissionString]) {
            "No name resource for permission ${permission.permissionString}"
        })

    data class PermissionUiItem(
        val status: PermissionStatus,
        @StringRes val nameRes: Int,
    )

    data class State(
        val isLoading: Boolean,
        val readPermissions: List<PermissionUiItem>,
        val writePermissions: List<PermissionUiItem>,
    ) {
        val hasAnyDenied: Boolean
            get() = (readPermissions + writePermissions).any { !it.status.isGranted }
    }

    sealed class Event {
        data object Refresh : Event()
        data class RequestPermission(val permission: HealthPermission) : Event()
        data object RequestAllMissing : Event()
    }
}
