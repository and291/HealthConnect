package com.example.healthconnect.utilty.impl.ui.screen.record

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun BasalBodyTemperatureScreen(
    initialRecord: BasalBodyTemperatureRecord,
    modifier: Modifier = Modifier,
    componentProvider: ComponentProvider = Di.componentProvider,
    viewModel: BasalBodyTemperatureViewModel = viewModel(
        modelClass = BasalBodyTemperatureViewModel::class,
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(BasalBodyTemperatureViewModel.RECORD_KEY, initialRecord)
        }
    )
) {

    val effect by viewModel.effect.collectAsState(null)
    LaunchedEffect(effect) {
        effect?.let { modification ->
            when (modification) {
                is BasalBodyTemperatureViewModel.Effect.RecordUpdated -> {
                    Log.d(this::class.simpleName, "Record updated!")
                }
            }
            viewModel.effectConsumed(modification)
        }
    }

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
            viewModel.state.measurementLocation
        ) {
            viewModel.onEvent(Event.OnMeasurementLocationSelected(it))
        }

        componentProvider.MetadataEditor(
            viewModel.state.metadataEditorModel
        ) {
            viewModel.onEvent(Event.OnMetaModelChanged(it))
        }

        Row {
            if (viewModel.isChanged) {
                Text("There is unsaved changes")
            }

            Button(
                enabled = viewModel.isChanged,
                onClick = { viewModel.onEvent(Event.OnSave) }
            ) {
                Text("Save")
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
