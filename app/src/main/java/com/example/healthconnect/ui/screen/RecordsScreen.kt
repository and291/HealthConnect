package com.example.healthconnect.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import kotlin.reflect.KClass


@Composable
fun RecordsScreen(
    requestPermission: (String) -> Unit,
    recordType: KClass<Record>,
    modifier: Modifier = Modifier,
    viewModel: RecordsViewModel = viewModel(
        key = RecordsViewModel::class.qualifiedName + recordType,
        modelClass = RecordsViewModel::class.java,
        factory = Di.recordsViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(RecordsViewModel.RECORD_TYPE_KEY, recordType)
        }
    )
) {

    val effect by viewModel.effect.collectAsState(null)

    LaunchedEffect(effect) {
        effect?.let { modification ->
            when (modification) {
                is RecordsViewModel.Effect.RequestSinglePermission -> requestPermission(modification.sdkPermission)
            }
            viewModel.effectConsumed(modification)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
    ) {

        when (val state = viewModel.state) {
            is RecordsViewModel.State.Data -> {
                items(state.records) { record ->
                    Text(record)
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
        recordType = StepsRecord::class as KClass<Record>
    )
}
