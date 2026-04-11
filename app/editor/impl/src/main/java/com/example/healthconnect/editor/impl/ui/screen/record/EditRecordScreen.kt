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
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.metadata.Metadata
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.editor.impl.di.Di
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel.Event
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordViewModel.State
import com.example.healthconnect.models.api.domain.record.BasalBodyTemperature
import com.example.healthconnect.models.api.domain.record.Model
import java.time.Instant
import java.time.ZoneOffset


@Composable
fun EditRecordScreen(
    model: Model,
    modifier: Modifier = Modifier,
    componentFactory: ComponentFactory = Di.componentFactory,
    viewModel: EditRecordViewModel = viewModel(
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(EditRecordViewModel.RECORD_KEY, model)
        }
    )
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(16.dp)
    ) {
        with(componentFactory) {
            create(viewModel.sortedFields) {
                viewModel.onEvent(it)
            }
        }

        item {
            Column {
                if (viewModel.isChanged) {
                    Text("There is unsaved changes")
                }

                when (val state = viewModel.state) {
                    is State.Edition, is State.UpdateResult -> Row {
                        Button(
                            enabled = viewModel.isChanged && viewModel.state.model.isValid(),
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
}

@Composable
@Preview(showBackground = true, heightDp = 1600)
private fun EditRecordScreenPreview() {
    EditRecordScreen(
        model = BasalBodyTemperature(
            metadata = MetadataField(
                recordingMethod = SelectorField(
                    value = Metadata.RECORDING_METHOD_UNKNOWN,
                    type = SelectorField.Type.RecordingMethod(),
                ),
                id = StringField(value = "", type = StringField.Type.MetadataId(), readOnly = true),
                dataOriginPackageName = StringField(value = "", type = StringField.Type.MetadataDataOrigin(), readOnly = true),
                lastModifiedTime = StringField(value = "", type = StringField.Type.MetadataLastModifiedTime(), readOnly = true),
                clientRecordId = StringField(value = "", type = StringField.Type.MetadataClientRecordId()),
                clientRecordVersion = StringField(value = "", type = StringField.Type.MetadataClientRecordVersion()),
            ),
            time = TimeField.Instantaneous(
                instant = Instant.EPOCH,
                zoneOffset = ZoneOffset.UTC,
            ),
            temperature = ValueField.Dbl(
                parsedValue = 36.6,
                type = ValueField.Type.Temperature(),
            ),
            measurementLocation = SelectorField(
                value = BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN,
                type = SelectorField.Type.MeasurementLocationBodyTemperature(),
            ),
        )
    )
}
