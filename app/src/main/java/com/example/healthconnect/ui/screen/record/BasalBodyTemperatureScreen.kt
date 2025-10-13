package com.example.healthconnect.ui.screen.record

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.component.SelectorComponent
import com.example.healthconnect.ui.screen.component.TimeComponent
import com.example.healthconnect.ui.screen.component.TimeComponentViewModel
import com.example.healthconnect.ui.screen.component.TimeComponentViewModel.Companion.TIME_MODEL_KEY
import com.example.healthconnect.ui.screen.component.metadata.MetadataEditorComponent
import com.example.healthconnect.ui.screen.component.metadata.MetadataEditorViewModel
import com.example.healthconnect.ui.screen.component.model.InstantModel
import com.example.healthconnect.ui.screen.component.model.TimeComponentModel
import com.example.healthconnect.ui.screen.record.BasalBodyTemperatureViewModel.Effect
import com.example.healthconnect.ui.screen.record.BasalBodyTemperatureViewModel.Event
import com.example.healthconnect.ui.screen.record.mapper.MeasurementLocationMapper
import com.example.healthconnect.ui.screen.record.mapper.RecordMapper
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun BasalBodyTemperatureScreen(
    record: BasalBodyTemperatureRecord,
    modifier: Modifier = Modifier,
    recordMapper: RecordMapper = Di.recordMapper,
    measurementLocationMapper: MeasurementLocationMapper = Di.measurementLocationMapper,
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
                is Effect.RecordUpdated -> {
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

        val timeComponentViewModel: TimeComponentViewModel = viewModel(
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(TIME_MODEL_KEY, TimeComponentModel.create(
                    instant = record.time,
                    zoneOffset = record.zoneOffset,
                ))
            }
        )

        LaunchedEffect(timeComponentViewModel.state) {
            Log.d(this::class.simpleName, "Metadata: ${timeComponentViewModel.state}")
            val instantModel = when (val t  = timeComponentViewModel.state.timeModel) {
                is TimeComponentModel.TimeModel.Invalid -> InstantModel.Invalid
                is TimeComponentModel.TimeModel.Valid -> InstantModel.Valid(
                    instant = t.instant,
                    zoneOffset = timeComponentViewModel.state.zoneId?.rules?.getOffset(t.instant)
                )
            }

            val event = Event.OnTimeChanged(instantModel)
            viewModel.onEvent(event)
        }

        TimeComponent(
            modifier = Modifier.fillMaxWidth(),
            viewModel =  timeComponentViewModel,
        )

        OutlinedTextField(
            value = viewModel.state.temperature.inCelsius.toString(), //TODO create a temperature editor view
            enabled = true,
            singleLine = true,
            onValueChange = {
                viewModel.onEvent(Event.OnTemperatureChanged(it))
            },
            label = {
                Text("Temperature")
            },
            supportingText = {
                Text("Temperature in \"Temperature\" unit. Required field. Valid range: 0-100 Celsius degrees")
            },
            modifier = Modifier.fillMaxWidth()
        )

        SelectorComponent(
            title = "Measurement Location",
            supportText = "Where on the user's basal body the temperature measurement was taken from. Optional field.",
            selectedText = measurementLocationMapper.map(viewModel.state.measurementLocation),
            items = measurementLocationMapper.locations,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = { (locationType, _) ->
                viewModel.onEvent(Event.OnMeasurementLocationSelected(locationType))
            }
        )

        val metadataViewModel: MetadataEditorViewModel = viewModel(
            modelClass = MetadataEditorViewModel::class,
            key = record.metadata.id, //TODO double-check if this a correct key to use for new viewmodel instance creation
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(MetadataEditorViewModel.METADATA_ENTITY_KEY, viewModel.state.metadataEntity)
            }
        )

        LaunchedEffect(metadataViewModel.state) {
            Log.d(this::class.simpleName, "Metadata: ${metadataViewModel.state}")
            viewModel.onEvent(
                Event.OnMetaModelChanged(
                    metadataViewModel.state
                )
            )
        }

        Text("Metadata:")
        MetadataEditorComponent(
            metadataEntity = viewModel.state.metadataEntity,
            viewModel = metadataViewModel
        )

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
