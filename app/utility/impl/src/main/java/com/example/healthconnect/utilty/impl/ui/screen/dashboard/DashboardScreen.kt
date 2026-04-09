package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.di.Di
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.DashboardViewModel.Effect
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.DashboardViewModel.Event
import kotlin.reflect.KClass

@Composable
fun DashboardScreen(
    onTypeClick: (KClass<out Model>, Int) -> Unit,
    onShowLibraryDataManager: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = Di.dashboardViewModelFactory),
) {
    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.Refresh)
    }

    val effect by viewModel.effect.collectAsState(null)
    LaunchedEffect(effect) {
        effect?.let { e ->
            when (e) {
                is Effect.NavigateToRecords -> onTypeClick(e.recordType, e.nameRes)
                is Effect.ShowLibraryDataManager -> onShowLibraryDataManager()
            }
            viewModel.effectConsumed(e)
        }
    }

    when (val state = viewModel.state) {
        is DashboardViewModel.State.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is DashboardViewModel.State.Data -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxSize(),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    OutlinedButton(
                        onClick = {
                            viewModel.onEvent(Event.OnLibraryDataManagerClick)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text("Health Connect's internal data manager")
                    }
                }
                state.segments.forEach { segment ->
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = segment.title,
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
                            onClick = {
                                viewModel.onEvent(
                                    Event.OnTypeClick(
                                        recordType = item.recordType,
                                        nameRes = item.nameRes,
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 480, heightDp = 720, showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    DashboardScreen(
        onTypeClick = { _, _ -> },
        onShowLibraryDataManager = {}
    )
}
