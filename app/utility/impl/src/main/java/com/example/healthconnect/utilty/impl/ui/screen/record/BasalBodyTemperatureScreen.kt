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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.celsius
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.utilty.impl.di.Di
import com.example.healthconnect.utilty.impl.ui.screen.record.BasalBodyTemperatureViewModel.Event
import com.example.healthconnect.utilty.impl.ui.screen.record.BasalBodyTemperatureViewModel.State
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun BasalBodyTemperatureScreen(
    initialRecord: BasalBodyTemperatureRecord,
    modifier: Modifier = Modifier,
    componentProvider: ComponentProvider = Di.componentProvider,
    viewModel: BasalBodyTemperatureViewModel = viewModel(
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(BasalBodyTemperatureViewModel.RECORD_KEY, initialRecord)
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
            zoneOffset = initialRecord.zoneOffset
        ) {
            viewModel.onEvent(Event.OnTimeChanged(it))
        }

        componentProvider.TemperatureEditor(
            TemperatureEditorModel.Valid(initialRecord.temperature.inCelsius)
        ) {
            viewModel.onEvent(Event.OnTemperatureChanged(it))
        }

        componentProvider.MeasurementLocationSelector(
            viewModel.state.basalBodyTemperatureEditorModel.measurementLocation
        ) {
            viewModel.onEvent(Event.OnMeasurementLocationSelected(it))
        }

        componentProvider.MetadataEditor(
            viewModel.state.basalBodyTemperatureEditorModel.metadataEditorModel
        ) {
            viewModel.onEvent(Event.OnMetaModelChanged(it))
        }

        Column {
            if (viewModel.isChanged) {
                Text("There is unsaved changes")
            }

            when (val state = viewModel.state) {
                is State.Edition, is State.UpdateResult -> Row {
                    Button(
                        enabled = viewModel.isChanged,
                        onClick = { viewModel.onEvent(Event.OnSave(upsert = false)) }
                    ) {
                        Text("Save")
                    }
                    if (state is State.UpdateResult) {
                        Text("Update Result: ${state.result}")
                    }
                    if (state is State.Edition && state.errorCreatingEntity != null) {
                        Text("Error creating entity: ${state.errorCreatingEntity}")
                    }
                }

                is State.UpdateInProgress -> {
                    CircularProgressIndicator()
                }
            }

        }
    }
}

@Composable
@Preview(showBackground = true, heightDp = 1600)
fun BasalBodyTemperatureScreenPreview() {

    BasalBodyTemperatureScreen(
        initialRecord = BasalBodyTemperatureRecord(
            time = Instant.EPOCH,
            zoneOffset = ZoneOffset.UTC,
            temperature = 36.celsius,
            measurementLocation = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
            metadata = Metadata.unknownRecordingMethod(),
        )
    )
}
