package com.example.healthconnect.permission_overview.ui.screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthconnect.permission_overview.api.domain.entity.Permission
import com.example.healthconnect.permission_overview.api.ui.PermissionContractProvider
import com.example.healthconnect.permission_overview.di.Locator
import com.example.healthconnect.permission_overview.entity.PermissionStatus
import com.example.healthconnect.permission_overview.ui.screen.PermissionsViewModel.Event
import com.example.healthconnect.permission_overview.ui.screen.PermissionsViewModel.PermissionUiModel

@Composable
internal fun PermissionsRoute(
    modifier: Modifier = Modifier,
    permissionContractProvider: PermissionContractProvider = Locator.impl.permissionContractProvider,
    viewModel: PermissionsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                PermissionsViewModel(
                    permissions = Locator.impl.permissions,
                    resolver = Locator.impl.resolver,
                    savedStateHandle = createSavedStateHandle(),
                )
            }
        }
    ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var activeRequestId by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = permissionContractProvider.getLibraryContract(),
    ) { grantedPermissionStrings ->
        viewModel.onEvent(
            Event.PermissionResult(
                requestId = activeRequestId,
                grantedPermissionStrings = grantedPermissionStrings,
            )
        )
        activeRequestId = null
    }

    LaunchedEffect(state.pendingPermissionRequest?.id) {
        val request = state.pendingPermissionRequest ?: return@LaunchedEffect

        activeRequestId = request.id
        viewModel.onEvent(Event.PermissionRequestLaunched(request.id))

        runCatching {
            permissionLauncher.launch(
                request.permissions.mapTo(mutableSetOf()) { it.value }
            )
        }.onFailure { error ->
            activeRequestId = null
            viewModel.onEvent(
                Event.PermissionRequestLaunchFailed(
                    requestId = request.id,
                    message = error.message,
                )
            )
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onEvent(Event.Refresh)
    }

    PermissionsScreen(
        state = state,
        modifier = modifier,
        onRequestPermission = { permission ->
            viewModel.onEvent(Event.RequestPermission(permission))
        },
        onRequestAllMissing = {
            viewModel.onEvent(Event.RequestAllMissing)
        },
    )

    val rationale = state.rationale
    if (rationale != null) {
        PermissionRationaleDialog(
            requestedPermissions = rationale.request.permissions,
            onConfirm = {
                viewModel.onEvent(Event.RationaleConfirmed(rationale.request.id))
            },
            onDismiss = {
                viewModel.onEvent(Event.RationaleDismissed(rationale.request.id))
            },
        )
    }

    val settingsDialog = state.openSettingsDialog
    if (settingsDialog != null) {
        OpenSettingsDialog(
            deniedPermissionStrings = settingsDialog.deniedPermissionStrings,
            onOpenHealthConnectSettings = {
                openSettings(context, permissionContractProvider.getLibrarySettingsAction())
                viewModel.onEvent(Event.SettingsOpened)
            },
            onOpenApplicationSettings = {
                openApplicationSettings(context)
                viewModel.onEvent(Event.SettingsOpened)
            },
            onDismiss = {
                viewModel.onEvent(Event.SettingsDialogDismissed)
            },
        )
    }
}

@Composable
private fun PermissionsScreen(
    state: PermissionsViewModel.PermissionsUiState,
    modifier: Modifier = Modifier,
    onRequestPermission: (Permission) -> Unit,
    onRequestAllMissing: () -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    text = "Health Permissions",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f),
                )

                TextButton(onClick = onRequestAllMissing) {
                    Text("Grant missing")
                }
            }
        }

        if (state.errorMessage != null) {
            item {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }

        items(items = state.permissions) { model ->
            PermissionEntryRow(
                model = model,
                onRequestPermission = onRequestPermission,
            )
        }
    }
}

@Composable
private fun PermissionEntryRow(
    model: PermissionUiModel,
    onRequestPermission: (Permission) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = stringResource(model.nameRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )

        when (model) {
            is PermissionUiModel.Single -> {
                AccessIndicator(
                    label = "Read",
                    access = model.read,
                    onRequestPermission = onRequestPermission,
                )
            }

            is PermissionUiModel.ReadWrite -> {
                AccessIndicator(
                    label = "Read",
                    access = model.read,
                    onRequestPermission = onRequestPermission,
                )

                Spacer(modifier = Modifier.size(16.dp))

                AccessIndicator(
                    label = "Write",
                    access = model.write,
                    onRequestPermission = onRequestPermission,
                )
            }
        }
    }
}

@Composable
private fun AccessIndicator(
    label: String,
    access: PermissionsViewModel.AccessUiModel,
    onRequestPermission: (Permission) -> Unit,
    modifier: Modifier = Modifier,
) {
    val status = access.status

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
        )

        Spacer(modifier = Modifier.size(4.dp))

        Icon(
            imageVector = if (status.isGranted) Icons.Filled.Check else Icons.Filled.Close,
            contentDescription = if (status.isGranted) {
                "$label granted"
            } else {
                "$label denied"
            },
            tint = if (status.isGranted) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
            modifier = Modifier.size(20.dp),
        )

        if (!status.isGranted) {
            Spacer(modifier = Modifier.size(4.dp))

            TextButton(
                onClick = {
                    onRequestPermission(access.permission)
                }
            ) {
                Text("Grant")
            }
        }
    }
}

@Composable
private fun PermissionRationaleDialog(
    requestedPermissions: Set<Permission>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Health data access")
        },
        text = {
            Text(
                text = buildString {
                    append("The app needs access to selected Health Connect data types ")
                    append("to show and process your health data. You can grant or deny ")
                    append("each permission on the next screen.\n\n")
                    append("Requested permissions:\n")
                    requestedPermissions.forEach { permission ->
                        append("• ")
                        append(permission.value)
                        append('\n')
                    }
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not now")
            }
        },
    )
}

@Composable
private fun OpenSettingsDialog(
    deniedPermissionStrings: Set<String>,
    onOpenHealthConnectSettings: () -> Unit,
    onOpenApplicationSettings: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Permissions were not granted")
        },
        text = {
            Text(
                text = buildString {
                    append("Some permissions are still denied. You can grant them ")
                    append("manually in Health Connect settings.\n\n")
                    append("Denied permissions:\n")
                    deniedPermissionStrings.forEach { permission ->
                        append("• ")
                        append(permission)
                        append('\n')
                    }
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onOpenHealthConnectSettings) {
                Text("Health Connect settings")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onOpenApplicationSettings) {
                    Text("App settings")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PermissionsScreenPreview() {
    MaterialTheme {
        Surface {
            PermissionsScreen(
                state = PermissionsViewModel.PermissionsUiState(
                    permissions = listOf(
                        PermissionUiModel.ReadWrite(
                            nameRes = android.R.string.unknownName,
                            read = PermissionsViewModel.AccessUiModel(
                                permission = Permission("read_heart_rate"),
                                status = PermissionStatus("read_heart_rate", isGranted = true)
                            ),
                            write = PermissionsViewModel.AccessUiModel(
                                permission = Permission("write_heart_rate"),
                                status = PermissionStatus("write_heart_rate", isGranted = false)
                            )
                        ),
                        PermissionUiModel.Single(
                            nameRes = android.R.string.unknownName,
                            read = PermissionsViewModel.AccessUiModel(
                                permission = Permission("read_steps"),
                                status = PermissionStatus("read_steps", isGranted = false)
                            )
                        )
                    )
                ),
                onRequestPermission = {},
                onRequestAllMissing = {}
            )
        }
    }
}

private fun openSettings(context: Context, healthConnectAction: String) {
    val intent = Intent(healthConnectAction)

    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        openApplicationSettings(context)
    }
}

private fun openApplicationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }

    context.startActivity(intent)
}
