package com.example.healthconnect.utilty.impl.ui.screen.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.utilty.impl.di.Di
import com.example.healthconnect.utilty.impl.ui.screen.record.BasalMetabolicRateViewModel.State.*

@Composable
fun BasalMetabolicRateScreen(
    initialRecord: BasalMetabolicRateRecord,
    modifier: Modifier = Modifier,
    componentProvider: ComponentProvider = Di.componentProvider,
    viewModel: BasalMetabolicRateViewModel = viewModel(
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(BasalMetabolicRateViewModel.RECORD_KEY, initialRecord)
        }
    )
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        componentProvider.TimeEditor(
            time = initialRecord.time,
            zoneOffset = initialRecord.zoneOffset //TODO keep params consistent to other components
        ) {
            viewModel.onEvent(BasalMetabolicRateViewModel.Event.OnTimeChanged(it))
        }

        componentProvider.PowerEditor(
            viewModel.state.basalMetabolicRateEditorModel.powerEditorModel
        ) {
            viewModel.onEvent(BasalMetabolicRateViewModel.Event.OnPowerChanged(it))
        }

        componentProvider.MetadataEditor(
            viewModel.state.basalMetabolicRateEditorModel.metadataEditorModel
        ) {
            viewModel.onEvent(BasalMetabolicRateViewModel.Event.OnMetaModelChanged(it))
        }

        Column {
            if (viewModel.isChanged) {
                Text("There is unsaved changes")
            }

            when (val state = viewModel.state) {
                is Edition, is UpdateResult -> Row {
                    Button(
                        enabled = viewModel.isChanged,
                        onClick = { viewModel.onEvent(BasalMetabolicRateViewModel.Event.OnSave(upsert = false)) }
                    ) {
                        Text("Save")
                    }
                    if (state is UpdateResult) {
                        Text("Update Result: ${state.result}")
                    }
                    if (state is Edition && state.errorCreatingEntity != null) {
                        Text("Error creating entity: ${state.errorCreatingEntity}")
                    }
                }

                is UpdateInProgress -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}