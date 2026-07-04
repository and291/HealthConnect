package com.example.healthconnect.record_list.ui.screen.records

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthconnect.record_list.api.domain.entity.RecordModel
import com.example.healthconnect.record_list.api.ui.RecordSummaryFactory
import com.example.healthconnect.record_list.di.Locator
import com.example.healthconnect.record_list.ui.screen.records.RecordsViewModel.Effect
import com.example.healthconnect.record_list.ui.screen.records.RecordsViewModel.Event
import com.example.healthconnect.record_list.ui.screen.records.RecordsViewModel.State
import com.example.healthconnect.record_list.ui.screen.records.RecordsViewModel.State.DisplayPage
import kotlin.reflect.KClass

@Composable
internal fun RecordsScreen(
    onRecordClick: (RecordModel) -> Unit,
    onInsertRecordClick: () -> Unit,
    recordType: KClass<*>,
    title: String,
    modifier: Modifier = Modifier,
    summaryFactory: RecordSummaryFactory = Locator.impl.summaryFactory,
    viewModel: RecordsViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                RecordsViewModel(
                    modelType = recordType,
                    loadRecords = Locator.impl.loadRecords,
                    deleteRecord = Locator.impl.deleteRecord,
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
                    is Effect.OpenRecordScreen -> onRecordClick(e.record)
                }
            }
    }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    RecordsList(
        state = state,
        title = title,
        summaryFactory = summaryFactory,
        onRefresh = { viewModel.onEvent(Event.Refresh) },
        onInsertRecordClick = onInsertRecordClick,
        onRecordClick = { viewModel.onEvent(Event.OnRecordClick(it)) },
        onDeleteRecord = { record ->
            viewModel.onEvent(
                Event.DeleteRecord(
                    recordType = recordType,
                    metadataId = record.metadataId(),
                )
            )
        },
        onNextPage = { viewModel.onEvent(Event.NextPage) },
        modifier = modifier,
    )
}

@Composable
private fun RecordsList(
    state: State,
    title: String,
    summaryFactory: RecordSummaryFactory,
    onRefresh: () -> Unit,
    onInsertRecordClick: () -> Unit,
    onRecordClick: (RecordModel) -> Unit,
    onDeleteRecord: (RecordModel) -> Unit,
    onNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            item {
                Button(onClick = onInsertRecordClick) {
                    Text("+")
                }
            }
            state.pages.forEach { page ->
                when (page) {
                    is DisplayPage.Error -> item {
                        RecordError(message = page.message)
                    }

                    is DisplayPage.Record -> items(page.records) { record ->
                        RecordItem(
                            record = record,
                            summaryFactory = summaryFactory,
                            onDelete = { onDeleteRecord(record) },
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .clickable { onRecordClick(record) },
                        )
                    }

                    is DisplayPage.PermissionDenied -> item {
                        PermissionDeniedMessage(dataTypeName = title)
                    }
                }
            }
            if (state.hasMorePages) {
                item {
                    CircularProgressIndicator()
                    LaunchedEffect(Unit) {
                        onNextPage()
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionDeniedMessage(
    dataTypeName: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataTypeName permission required",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 720, showBackground = true)
private fun RecordsListPreview() {
    val previewSummaryFactory = object : RecordSummaryFactory {
        @Composable
        override fun Summary(record: RecordModel, modifier: Modifier) {
            Text(
                text = "Steps: 8500",
                style = MaterialTheme.typography.bodySmall,
                modifier = modifier,
            )
        }
    }
    RecordsList(
        state = State(
            pages = listOf(
                DisplayPage.Record(
                    records = List(3) { index ->
                        object : RecordModel {
                            override fun metadataId(): String = "record-$index"
                        }
                    },
                ),
                DisplayPage.PermissionDenied,
            ),
            hasMorePages = false,
            isRefreshing = false,
        ),
        title = "Steps",
        summaryFactory = previewSummaryFactory,
        onRefresh = {},
        onInsertRecordClick = {},
        onRecordClick = {},
        onDeleteRecord = {},
        onNextPage = {},
    )
}
