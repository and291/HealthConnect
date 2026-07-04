package com.example.healthconnect.dashboard.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthconnect.dashboard.di.Locator
import com.example.healthconnect.dashboard.ui.screen.dashboard.DashboardViewModel.Effect
import com.example.healthconnect.dashboard.ui.screen.dashboard.DashboardViewModel.Event
import com.example.healthconnect.dashboard.ui.screen.dashboard.DashboardViewModel.State
import com.example.healthconnect.dashboard.ui.screen.dashboard.model.DashboardItem
import com.example.healthconnect.dashboard.ui.screen.dashboard.model.DashboardSegment
import kotlin.reflect.KClass

@Composable
internal fun DashboardScreen(
    onTypeClick: (KClass<*>, Int) -> Unit,
    onShowLibraryDataManager: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                DashboardViewModel(
                    getCatalog = Locator.impl.getCatalog,
                    countRecords = Locator.impl.countRecords,
                )
            }
        }
    ),
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        viewModel.effect
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { e ->
                when (e) {
                    is Effect.NavigateToRecords -> onTypeClick(e.recordType, e.nameRes)
                    is Effect.ShowLibraryDataManager -> onShowLibraryDataManager()
                    is Effect.NavigateToPermissions -> onNavigateToPermissions()
                }
            }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    when (val uiState = state) {
        is State.Data -> DashboardGrid(
            state = uiState,
            onRefresh = { viewModel.onEvent(Event.Refresh) },
            onLibraryDataManagerClick = { viewModel.onEvent(Event.OnLibraryDataManagerClick) },
            onPermissionsClick = { viewModel.onEvent(Event.OnPermissionsClick) },
            onTypeClick = { item ->
                viewModel.onEvent(Event.OnTypeClick(recordType = item.recordType, nameRes = item.nameRes))
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun DashboardGrid(
    state: State.Data,
    onRefresh: () -> Unit,
    onLibraryDataManagerClick: () -> Unit,
    onPermissionsClick: () -> Unit,
    onTypeClick: (DashboardItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedButton(
                    onClick = onLibraryDataManagerClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text("Health Connect's internal data manager")
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedButton(
                    onClick = onPermissionsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text("Manage permissions")
                }
            }
            state.segments.forEach { segment ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(segment.title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
                items(segment.items) { item ->
                    DashboardTile(
                        item = item,
                        onClick = { onTypeClick(item) },
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 480, heightDp = 720, showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    fun item(state: DashboardItem.LoadingState) = DashboardItem(
        recordType = Any::class,
        nameRes = android.R.string.untitled,
        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
        state = state,
    )
    DashboardGrid(
        state = State.Data(
            segments = listOf(
                DashboardSegment(
                    title = android.R.string.untitled,
                    items = listOf(
                        item(DashboardItem.LoadingState.Counted(42)),
                        item(DashboardItem.LoadingState.InProgress),
                    ),
                ),
            ),
            isRefreshing = false,
        ),
        onRefresh = {},
        onLibraryDataManagerClick = {},
        onPermissionsClick = {},
        onTypeClick = {},
    )
}
