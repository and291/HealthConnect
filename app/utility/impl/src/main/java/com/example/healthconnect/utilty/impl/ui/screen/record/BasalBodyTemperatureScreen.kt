package com.example.healthconnect.utilty.impl.ui.screen.record

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.healthconnect.components.api.ui.model.TemperatureModel
import com.example.healthconnect.utilty.impl.di.Di
import com.example.healthconnect.utilty.impl.ui.screen.record.BasalBodyTemperatureViewModel.Event
import com.example.healthconnect.utilty.impl.ui.screen.record.mapper.RecordMapper
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun BasalBodyTemperatureScreen(
    record: BasalBodyTemperatureRecord,
    modifier: Modifier = Modifier,
    recordMapper: RecordMapper = Di.recordMapper,
    componentProvider: ComponentProvider = Di.componentProvider,
    viewModel: BasalBodyTemperatureViewModel = viewModel(
        modelClass = BasalBodyTemperatureViewModel::class,
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(BasalBodyTemperatureViewModel.RECORD_KEY, recordMapper.toUiModel(record))
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

        componentProvider.Time(
            time = record.time,
            zoneOffset = record.zoneOffset
        ) {
            viewModel.onEvent(Event.OnTimeChanged(it))
        }

        componentProvider.Temperature(
            TemperatureModel.Valid(record.temperature.inCelsius)
        ) {
            viewModel.onEvent(Event.OnTemperatureChanged(it))
        }

        componentProvider.MeasurementLocationSelector(
            viewModel.state.measurementLocation
        ) {
            viewModel.onEvent(Event.OnMeasurementLocationSelected(it))
        }

        componentProvider.MetadataEditor(
            viewModel.state.metadataEntity
        ) {
            viewModel.onEvent(Event.OnMetaModelChanged(it))
        }

        //TODO show the button only if there are changes to save
        Button(onClick = {
            viewModel.onEvent(Event.OnSave)
        }) {
            Text("Save changes")
        }
    }
}

@Composable
@Preview(showBackground = true, heightDp = 1600)
fun BasalBodyTemperatureScreenPreview() {

    BasalBodyTemperatureScreen(
        record = BasalBodyTemperatureRecord(
            time = Instant.EPOCH,
            zoneOffset = ZoneOffset.UTC,
            temperature = 36.celsius,
            measurementLocation = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
            metadata = Metadata.unknownRecordingMethod(),
        )
    )
}
