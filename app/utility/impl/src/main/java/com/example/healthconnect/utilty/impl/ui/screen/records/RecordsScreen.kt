package com.example.healthconnect.utilty.impl.ui.screen.records

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.di.Di
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.Effect
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.Event
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.State.DisplayPage
import kotlin.reflect.KClass


@Composable
fun RecordsScreen(
    requestPermission: (String) -> Unit,
    onRecordClick: (Model) -> Unit,
    onInsertRecordClick: () -> Unit,
    recordType: KClass<out Model>,
    title: String,
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel = viewModel(
        factory = Di.recordsViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(RecordsViewModel.RECORD_TYPE_KEY, recordType)
        }
    ),
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        viewModel.effect
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { e ->
                when (e) {
                    is Effect.RequestSinglePermission -> requestPermission(e.sdkPermission)
                    is Effect.OpenRecordScreen -> onRecordClick(e.record)
                }
            }
    }

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.onEvent(Event.Refresh) },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize(),
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
                            onDelete = {
                                val event = Event.DeleteRecord(
                                    recordType = recordType,
                                    metadataId = record.metadata.id.value
                                )
                                viewModel.onEvent(event)
                            },
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .clickable { onRecordClick(record) }
                        )
                    }
                }
            }
            if (state.hasMorePages) {
                item {
                    CircularProgressIndicator()
                    LaunchedEffect(Unit) {
                        viewModel.onEvent(Event.NextPage)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(widthDp = 480, heightDp = 720, showBackground = true)
fun RecordsScreenPreview() {
    @Suppress("UNCHECKED_CAST")
    RecordsScreen(
        requestPermission = {},
        onRecordClick = {},
        onInsertRecordClick = {},
        recordType = Steps::class,
        title = "Steps",
    )
}
