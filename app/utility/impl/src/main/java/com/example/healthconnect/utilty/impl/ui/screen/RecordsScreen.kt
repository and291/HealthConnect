package com.example.healthconnect.utilty.impl.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.utilty.impl.di.Di
import com.example.healthconnect.utilty.impl.ui.RecordItem
import kotlin.reflect.KClass


@Composable
fun RecordsScreen(
    requestPermission: (String) -> Unit,
    onRecordClick: (Record) -> Unit,
    onInsertRecordClick: () -> Unit,
    recordType: KClass<out Record>,
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel = viewModel(
        factory = Di.recordsViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(RecordsViewModel.RECORD_TYPE_KEY, recordType)
        }
    )
) {

    val effect by viewModel.effect.collectAsState(null)

    LaunchedEffect("") {
        viewModel.onEvent(RecordsViewModel.Event.Refresh)
    }

    LaunchedEffect(effect) {
        effect?.let { modification ->
            when (modification) {
                is RecordsViewModel.Effect.RequestSinglePermission -> requestPermission(modification.sdkPermission)
                is RecordsViewModel.Effect.OpenRecordScreen -> onRecordClick(modification.record)
            }
            viewModel.effectConsumed(modification)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {

        when (val state = viewModel.state) {
            is RecordsViewModel.State.Data -> {
                item {
                    Button(onClick = onInsertRecordClick) {
                        Text("+")
                    }
                }
                items(state.records) { record ->
                    RecordItem(
                        record = record,
                        onDelete = {
                            val event = RecordsViewModel.Event.DeleteRecord(
                                recordType = recordType,
                                metadataId = record.metadataId,
                            )
                            viewModel.onEvent(event)
                        },
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .clickable {
                                //Do you really need to route this event thru view model? What for?
                                val event = RecordsViewModel.Event.OnRecordClick(
                                    record = record.record,
                                )
                                viewModel.onEvent(event)
                            }
                    )
                }
            }

            RecordsViewModel.State.Loading -> {
                item {
                    CircularProgressIndicator()
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
        recordType = StepsRecord::class as KClass<Record>
    )
}
