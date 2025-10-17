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
import com.example.healthconnect.components.api.ui.model.MetadataModel
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.InstantModel
import com.example.healthconnect.components.api.ui.model.TemperatureModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorComponent
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorViewModel
import com.example.healthconnect.components.impl.ui.model.TimeComponentModel
import java.time.Instant
import java.time.ZoneOffset

class ComponentProviderImpl : ComponentProvider {

    @Composable
    override fun Time(
        time: Instant,
        zoneOffset: ZoneOffset?,
        onTimeChanged: (InstantModel) -> Unit
    ) {
        val timeComponentViewModel: TimeComponentViewModel = viewModel(
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(
                    TimeComponentViewModel.Companion.TIME_MODEL_KEY, TimeComponentModel.create(
                        instant = time,
                        zoneOffset = zoneOffset,
                    )
                )
            }
        )

        LaunchedEffect(timeComponentViewModel.state) {
            Log.d(this::class.simpleName, "Metadata: ${timeComponentViewModel.state}")
            val instantModel = when (val t = timeComponentViewModel.state.timeModel) {
                is TimeComponentModel.TimeModel.Invalid -> InstantModel.Invalid
                is TimeComponentModel.TimeModel.Valid -> InstantModel.Valid(
                    instant = t.instant,
                    zoneOffset = timeComponentViewModel.state.zoneId?.rules?.getOffset(t.instant)
                )
            }

            onTimeChanged(instantModel)
        }

        TimeComponent(
            modifier = Modifier.fillMaxWidth(),
            viewModel = timeComponentViewModel,
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
        metadataModel: MetadataModel,
        onMetadataChanged: (MetadataModel) -> Unit,
    ) {

        val metadataViewModel: MetadataEditorViewModel = viewModel(
            key = metadataModel.id, //TODO double-check if this a correct key to use for new viewmodel instance creation
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(MetadataEditorViewModel.Companion.METADATA_ENTITY_KEY, metadataModel)
            }
        )

        LaunchedEffect(metadataViewModel.state) {
            Log.d(this::class.simpleName, "Metadata: ${metadataViewModel.state}")
            onMetadataChanged(metadataViewModel.state)
        }

        Text("Metadata:")
        MetadataEditorComponent(
            metadataModel = metadataModel,
            viewModel = metadataViewModel
        )
    }

    @Composable
    override fun Temperature(
        temperatureModel: TemperatureModel,
        onTemperatureChanged: (TemperatureModel) -> Unit
    ) {
        val viewModel: TemperatureComponentViewModel = viewModel(
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(TemperatureComponentViewModel.TEMPERATURE_MODEL_KEY, temperatureModel)
            }
        )

        LaunchedEffect(viewModel.state) {
            Log.d(this::class.simpleName, "Temperature: ${viewModel.state}")
            onTemperatureChanged(viewModel.state)
        }
        TemperatureComponent(
            temperatureModel = temperatureModel,
            viewModel = viewModel,
        )
    }
}