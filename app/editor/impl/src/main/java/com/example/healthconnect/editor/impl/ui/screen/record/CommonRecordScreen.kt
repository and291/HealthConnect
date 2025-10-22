package com.example.healthconnect.editor.impl.ui.screen.record

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
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.celsius
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.editor.api.ui.model.Event
import com.example.healthconnect.editor.impl.di.Di
import com.example.healthconnect.editor.impl.ui.screen.record.CommonRecordViewModel.State
import java.time.Instant
import java.time.ZoneOffset


@Composable
fun CommonRecordScreen(
    initialRecord: Record,
    modifier: Modifier = Modifier,
    componentFactory: ComponentFactory = Di.componentFactory,
    viewModel: CommonRecordViewModel = viewModel(
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(CommonRecordViewModel.RECORD_KEY, initialRecord)
        }
    )
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        componentFactory.Create(viewModel.state.editorModel) {
            viewModel.onEvent(it)
        }

        Column {
            if (viewModel.isChanged) {
                Text("There is unsaved changes")
            }

            when (val state = viewModel.state) {
                is State.Edition, is State.UpdateResult -> Row {
                    Button(
                        enabled = viewModel.isChanged,
                        onClick = { viewModel.onEvent(Event.OnUpdate(upsert = false)) }
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
fun CommonRecordScreenPreview() {

    CommonRecordScreen(
        initialRecord = BasalBodyTemperatureRecord(
            time = Instant.EPOCH,
            zoneOffset = ZoneOffset.UTC,
            temperature = 36.celsius,
            measurementLocation = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
            metadata = Metadata.unknownRecordingMethod(),
        )
    )
}
