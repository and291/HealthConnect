package com.example.healthconnect.permission_overview.ui.screen

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthconnect.permission_overview.api.domain.PermissionResolver
import com.example.healthconnect.permission_overview.api.domain.entity.Permission
import com.example.healthconnect.permission_overview.api.domain.entity.PermissionEntry
import com.example.healthconnect.permission_overview.api.domain.entity.ReadWrite
import com.example.healthconnect.permission_overview.api.domain.entity.Single
import com.example.healthconnect.permission_overview.entity.PermissionStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

internal class PermissionsViewModel(
    private val permissions: Set<PermissionEntry>,
    private val resolver: PermissionResolver,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    data class PermissionsUiState(
        val permissions: List<PermissionUiModel> = emptyList(),
        val isLoading: Boolean = false,
        val pendingPermissionRequest: PermissionRequest? = null,
        val rationale: PermissionRationale? = null,
        val openSettingsDialog: OpenSettingsDialog? = null,
        val errorMessage: String? = null,
    )

    data class PermissionRequest(
        val id: Long,
        val permissions: Set<Permission>,
    )

    data class PermissionRationale(
        val request: PermissionRequest,
    )

    data class OpenSettingsDialog(
        val requestId: Long,
        val deniedPermissionStrings: Set<String>,
    )

    private val _state = MutableStateFlow(PermissionsUiState())
    val state: StateFlow<PermissionsUiState> = _state.asStateFlow()

    init {
        onEvent(Event.Refresh)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.Refresh -> refreshPermissions()
            is Event.RequestPermission -> requestPermissions(setOf(event.permission))
            is Event.RequestPermissions -> requestPermissions(event.permissions)
            is Event.RequestAllMissing -> requestPermissions(permissions.allPermissions())
            is Event.RationaleConfirmed -> {
                val rationale = _state.value.rationale ?: return
                if (rationale.request.id != event.requestId) return

                markPermissionsAsked(rationale.request.permissions)

                _state.update {
                    it.copy(
                        rationale = null,
                        pendingPermissionRequest = rationale.request,
                    )
                }
            }

            is Event.RationaleDismissed -> {
                _state.update { state ->
                    if (state.rationale?.request?.id == event.requestId) {
                        state.copy(rationale = null)
                    } else {
                        state
                    }
                }
            }

            is Event.PermissionRequestLaunched -> {
                val request = _state.value.pendingPermissionRequest ?: return
                if (request.id != event.requestId) return

                saveActiveRequest(request)

                _state.update {
                    it.copy(pendingPermissionRequest = null)
                }
            }

            is Event.PermissionRequestLaunchFailed -> {
                clearActiveRequest()

                _state.update {
                    it.copy(
                        pendingPermissionRequest = null,
                        errorMessage = event.message ?: "Permission request launch failed",
                    )
                }
            }

            is Event.PermissionResult -> onPermissionResult(
                requestId = event.requestId,
                grantedPermissionStrings = event.grantedPermissionStrings,
            )

            is Event.SettingsDialogDismissed -> {
                _state.update {
                    it.copy(openSettingsDialog = null)
                }
            }

            is Event.SettingsOpened -> {
                _state.update {
                    it.copy(openSettingsDialog = null)
                }
                refreshPermissions()
            }
        }
    }

    private var refreshPermissionsJob: Job? = null
    private fun refreshPermissions() {
        refreshPermissionsJob?.cancel() //TODO(dive depper with .cancel() vs .cancelAndJoin())
        refreshPermissionsJob = viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                resolvePermissions()
            }.onSuccess { models ->
                _state.update {
                    it.copy(
                        permissions = models,
                        isLoading = false,
                        errorMessage = null,
                    )
                }
            }.onFailure { error ->
                if (error is CancellationException) throw error
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to resolve permissions",
                    )
                }
            }
        }
    }

    private fun requestPermissions(requestedPermissions: Set<Permission>) {
        viewModelScope.launch {
            val missingPermissions = runCatching {
                findMissingPermissions(requestedPermissions)
            }.getOrElse { error ->
                if (error is CancellationException) throw error
                _state.update {
                    it.copy(errorMessage = error.message ?: "Failed to check permissions")
                }
                return@launch
            }

            if (missingPermissions.isEmpty()) {
                refreshPermissions()
                return@launch
            }

            val request = PermissionRequest(
                id = nextRequestId(),
                permissions = missingPermissions,
            )

            _state.update {
                it.copy(
                    rationale = PermissionRationale(request = request),
                    errorMessage = null,
                )
            }
        }
    }

    private suspend fun findMissingPermissions(
        requestedPermissions: Set<Permission>,
    ): Set<Permission> {
        val grants = resolver.isGranted(requestedPermissions)
            .associate { (permission, isGranted) -> permission to isGranted.boolean }

        return requestedPermissions.filterTo(mutableSetOf()) { permission -> grants[permission] != true }
    }

    private fun nextRequestId(): Long {
        val current = savedStateHandle[KEY_NEXT_REQUEST_ID] ?: 0L
        savedStateHandle[KEY_NEXT_REQUEST_ID] = current + 1L
        return current
    }

    private fun markPermissionsAsked(permissions: Set<Permission>) {
        val current = savedStateHandle
            .get<ArrayList<String>>(KEY_ASKED_PERMISSION_STRINGS)
            ?.toSet()
            .orEmpty()

        val updated = current + permissions.map { it.value }

        savedStateHandle[KEY_ASKED_PERMISSION_STRINGS] = ArrayList(updated)
    }

    private fun saveActiveRequest(request: PermissionRequest) {
        savedStateHandle[KEY_ACTIVE_REQUEST_ID] = request.id
        savedStateHandle[KEY_ACTIVE_REQUEST_PERMISSION_STRINGS] =
            ArrayList(request.permissions.map { it.value })
    }

    private fun clearActiveRequest() {
        savedStateHandle.remove<Long>(KEY_ACTIVE_REQUEST_ID)
        savedStateHandle.remove<ArrayList<String>>(KEY_ACTIVE_REQUEST_PERMISSION_STRINGS)
    }

    private fun readActiveRequest(): PermissionRequest? {
        val id = savedStateHandle.get<Long>(KEY_ACTIVE_REQUEST_ID) ?: return null

        val permissionStrings = savedStateHandle
            .get<ArrayList<String>>(KEY_ACTIVE_REQUEST_PERMISSION_STRINGS)
            ?.toSet()
            .orEmpty()

        val configuredPermissionsByValue = permissions
            .allPermissions()
            .associateBy { it.value }

        val activePermissions = permissionStrings
            .mapNotNull { configuredPermissionsByValue[it] }
            .toSet()

        if (activePermissions.isEmpty()) return null

        return PermissionRequest(
            id = id,
            permissions = activePermissions,
        )
    }

    private fun onPermissionResult(
        requestId: Long?,
        grantedPermissionStrings: Set<String>,
    ) {
        val activeRequest = readActiveRequest()

        clearActiveRequest()

        if (requestId == null || activeRequest == null || activeRequest.id != requestId) {
            refreshPermissions()
            return
        }

        val requestedPermissionStrings = activeRequest.permissions.mapTo(mutableSetOf()) {
            it.value
        }

        val deniedPermissionStrings = requestedPermissionStrings - grantedPermissionStrings

        if (deniedPermissionStrings.isNotEmpty()) {
            _state.update {
                it.copy(
                    openSettingsDialog = OpenSettingsDialog(
                        requestId = activeRequest.id,
                        deniedPermissionStrings = deniedPermissionStrings,
                    )
                )
            }
        }

        refreshPermissions()
    }

    /**
     * Resolves every configured [PermissionEntry] into a [PermissionUiModel].
     * All underlying [Permission]s are queried in a single [resolver] call and
     * the grant statuses are then looked up per entry.
     */
    private suspend fun resolvePermissions(): List<PermissionUiModel> {
        val grants = resolver.isGranted(permissions.allPermissions())
            .associate { (permission, isGranted) ->
                permission to isGranted.boolean
            }

        return permissions.map { entry ->
            when (entry) {
                is ReadWrite -> PermissionUiModel.ReadWrite(
                    nameRes = entry.nameResId,
                    read = entry.read.toAccessUiModel(grants),
                    write = entry.write.toAccessUiModel(grants),
                )

                is Single -> PermissionUiModel.Single(
                    nameRes = entry.nameResId,
                    read = entry.read.toAccessUiModel(grants),
                )
            }
        }
    }

    private fun Permission.toAccessUiModel(
        grants: Map<Permission, Boolean>,
    ): AccessUiModel = AccessUiModel(
        permission = this,
        status = PermissionStatus(
            permissionString = value,
            isGranted = grants[this] == true,
        ),
    )

    private fun Set<PermissionEntry>.allPermissions(): Set<Permission> =
        flatMapTo(mutableSetOf()) { entry ->
            when (entry) {
                is ReadWrite -> listOf(entry.read, entry.write)
                is Single -> listOf(entry.read)
            }
        }

    private fun Permission.toStatus(
        grants: Map<Permission, Boolean>,
    ): PermissionStatus = PermissionStatus(
        permissionString = value,
        isGranted = grants[this] == true,
    )

    data class AccessUiModel(
        val permission: Permission,
        val status: PermissionStatus,
    )

    /**
     * What the [PermissionsRoute] renders for a single row. Mirrors the two [PermissionEntry]
     * variants: an entry that exposes both read and write access, and one that exposes read only.
     */
    sealed interface PermissionUiModel {
        @get:StringRes
        val nameRes: Int

        data class ReadWrite(
            @param:StringRes
            override val nameRes: Int,
            val read: AccessUiModel,
            val write: AccessUiModel,
        ) : PermissionUiModel

        data class Single(
            @param:StringRes
            override val nameRes: Int,
            val read: AccessUiModel,
        ) : PermissionUiModel
    }

    sealed class Event {
        data object Refresh : Event()

        data class RequestPermission(
            val permission: Permission,
        ) : Event()

        data class RequestPermissions(
            val permissions: Set<Permission>,
        ) : Event()

        data object RequestAllMissing : Event()

        data class RationaleConfirmed(
            val requestId: Long,
        ) : Event()

        data class RationaleDismissed(
            val requestId: Long,
        ) : Event()

        data class PermissionRequestLaunched(
            val requestId: Long,
        ) : Event()

        data class PermissionRequestLaunchFailed(
            val requestId: Long,
            val message: String?,
        ) : Event()

        data class PermissionResult(
            val requestId: Long?,
            val grantedPermissionStrings: Set<String>,
        ) : Event()

        data object SettingsDialogDismissed : Event()
        data object SettingsOpened : Event()
    }

    private companion object {
        const val KEY_NEXT_REQUEST_ID = "permissions.next_request_id"
        const val KEY_ASKED_PERMISSION_STRINGS = "permissions.asked_permission_strings"
        const val KEY_ACTIVE_REQUEST_ID = "permissions.active_request_id"
        const val KEY_ACTIVE_REQUEST_PERMISSION_STRINGS =
            "permissions.active_request_permission_strings"
    }
}
