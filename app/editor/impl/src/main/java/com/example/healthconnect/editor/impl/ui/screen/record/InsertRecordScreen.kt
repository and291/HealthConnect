package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.healthconnect.editor.impl.di.Di
import com.example.healthconnect.editor.impl.ui.screen.record.InsertRecordViewModel.Event
import com.example.healthconnect.editor.impl.ui.screen.record.InsertRecordViewModel.State
import java.time.Instant
import java.time.ZoneOffset
import kotlin.reflect.KClass


@Composable
fun InsertRecordScreen(
    recordClass: KClass<Record>,
    modifier: Modifier = Modifier,
    componentFactory: ComponentFactory = Di.componentFactory,
    viewModel: InsertRecordViewModel = viewModel(
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(InsertRecordViewModel.RECORD_CLASS_KEY, recordClass)
        }
    ),
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(16.dp)
    ) {
        with(componentFactory) {
            create(viewModel.state.model) {
                viewModel.onEvent(it)
            }
        }

        item {
            Column {
                when (val state = viewModel.state) {
                    is State.Edition, is State.InsertResult -> Row {
                        Button(
                            enabled = viewModel.state.model.isValid(),
                            onClick = { viewModel.onEvent(Event.OnInsert) }
                        ) {
                            Text("Insert")
                        }
                        if (state is State.InsertResult) {
                            Text("Insert Result: ${state.result}")
                        }
                        if (state is State.Edition && state.errorCreatingEntity != null) {
                            Text("Error creating entity: ${state.errorCreatingEntity}")
                        }
                    }

                    is State.InsertInProgress -> {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, heightDp = 1600)
fun InsertRecordScreenPreview() {

    EditRecordScreen(
        initialRecord = BasalBodyTemperatureRecord(
            time = Instant.EPOCH,
            zoneOffset = ZoneOffset.UTC,
            temperature = 36.celsius,
            measurementLocation = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
            metadata = Metadata.unknownRecordingMethod(),
        )
    )
}
