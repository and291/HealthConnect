package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.impl.data.mapper.MeasurementLocationMapper
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorComponent
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorViewModel
import com.example.healthconnect.components.impl.ui.model.TimeEditorComponentModel
import java.time.Instant
import java.time.ZoneOffset

class ComponentProviderImpl : ComponentProvider {

    @Composable
    override fun TimeEditor(
        time: Instant,
        zoneOffset: ZoneOffset?,
        onTimeChanged: (TimeEditorModel) -> Unit
    ) {
        val timeEditorComponentViewModel: TimeEditorComponentViewModel = viewModel(
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(
                    TimeEditorComponentViewModel.Companion.TIME_MODEL_KEY, TimeEditorComponentModel.create(
                        instant = time,
                        zoneOffset = zoneOffset,
                    )
                )
            }
        )

        LaunchedEffect(timeEditorComponentViewModel.state) {
            Log.d(this::class.simpleName, "Metadata: ${timeEditorComponentViewModel.state}")
            val timeEditorModel = when (val t = timeEditorComponentViewModel.state.timeModel) {
                is TimeEditorComponentModel.TimeModel.Invalid -> TimeEditorModel.Invalid
                is TimeEditorComponentModel.TimeModel.Valid -> TimeEditorModel.Valid(
                    instant = t.instant,
                    zoneOffset = timeEditorComponentViewModel.state.zoneId?.rules?.getOffset(t.instant)
                )
            }

            onTimeChanged(timeEditorModel)
        }

        TimeEditorComponent(
            modifier = Modifier.fillMaxWidth(),
            viewModel = timeEditorComponentViewModel,
        )
    }

    @Composable
    override fun MeasurementLocationSelector(
        currentMeasurementLocation: Int,
        onItemSelected: (Int) -> Unit,
    ) {

        val measurementLocationMapper: MeasurementLocationMapper = Di.measurementLocationMapper

        SelectorComponent(
            title = "Measurement Location",
            supportText = "Where on the user's basal body the temperature measurement was taken from. Optional field.",
            selectedText = measurementLocationMapper.map(currentMeasurementLocation),
            items = measurementLocationMapper.locations,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = { (locationType, _) -> onItemSelected(locationType) }
        )
    }

    @Composable
    override fun MetadataEditor(
        metadataEditorModel: MetadataEditorModel,
        onMetadataChanged: (MetadataEditorModel) -> Unit,
    ) {

        val metadataViewModel: MetadataEditorViewModel = viewModel(
            key = metadataEditorModel.id, //TODO double-check if this a correct key to use for new viewmodel instance creation
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(MetadataEditorViewModel.Companion.METADATA_ENTITY_KEY, metadataEditorModel)
            }
        )

        LaunchedEffect(metadataViewModel.state) {
            Log.d(this::class.simpleName, "Metadata: ${metadataViewModel.state}")
            onMetadataChanged(metadataViewModel.state)
        }

        Text("Metadata:")
        MetadataEditorComponent(
            metadataEditorModel = metadataEditorModel,
            viewModel = metadataViewModel
        )
    }

    @Composable
    override fun TemperatureEditor(
        temperatureEditorModel: TemperatureEditorModel,
        onTemperatureChanged: (TemperatureEditorModel) -> Unit
    ) {
        val viewModel: TemperatureEditorComponentViewModel = viewModel(
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(TemperatureEditorComponentViewModel.TEMPERATURE_MODEL_KEY, temperatureEditorModel)
            }
        )

        LaunchedEffect(viewModel.state) {
            Log.d(this::class.simpleName, "Temperature: ${viewModel.state}")
            onTemperatureChanged(viewModel.state)
        }
        TemperatureEditorComponent(
            temperatureEditorModel = temperatureEditorModel,
            viewModel = viewModel,
        )
    }
}