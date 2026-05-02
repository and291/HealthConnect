package com.example.healthconnect.permissions.impl.ui.screen

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.permissions.impl.di.Di
import com.example.healthconnect.permissions.impl.ui.screen.PermissionsViewModel.Event
import com.example.healthconnect.permissions.impl.ui.screen.PermissionsViewModel.PermissionUiItem

@Composable
fun PermissionsScreen(
    modifier: Modifier = Modifier,
    viewModel: PermissionsViewModel = viewModel(factory = Di.permissionsViewModelFactory),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { viewModel.onEvent(Event.Refresh) },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Text(
                    text = "Health Permissions",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            if (state.hasAnyDenied) {
                item {
                    Button(
                        onClick = { viewModel.onEvent(Event.RequestAllMissing) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                    ) {
                        Text("Grant all missing permissions")
                    }
                }
            }

            item {
                PermissionGroupHeader("Read")
            }
            items(state.readPermissions) { item ->
                PermissionRow(
                    item = item,
                    onRequest = { viewModel.onEvent(Event.RequestPermission(item.status.permission)) },
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                PermissionGroupHeader("Write")
            }
            items(state.writePermissions) { item ->
                PermissionRow(
                    item = item,
                    onRequest = { viewModel.onEvent(Event.RequestPermission(item.status.permission)) },
                )
            }
        }
    }
}

@Composable
private fun PermissionGroupHeader(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    )
}

@Composable
private fun PermissionRow(
    item: PermissionUiItem,
    onRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = stringResource(item.nameRes)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
    ) {
        Icon(
            imageVector = if (item.status.isGranted) Icons.Filled.Check else Icons.Filled.Close,
            contentDescription = if (item.status.isGranted) "Granted" else "Denied",
            tint = if (item.status.isGranted) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        if (!item.status.isGranted) {
            IconButton(onClick = onRequest) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Request $name",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Preview(widthDp = 480, heightDp = 800, showBackground = true)
@Composable
private fun PermissionsScreenPreview() {
    PermissionsScreen()
}
