package com.example.healthconnect.components.impl.ui.metadata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.api.ui.model.DeviceEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.impl.ui.SelectorComponent
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorViewModel.Event
import com.example.healthconnect.components.impl.ui.metadata.mapper.RecordingMethodMapper
import java.time.Instant

@Composable
internal fun MetadataEditorComponent(
    metadataEditorModel: MetadataEditorModel,
    modifier: Modifier = Modifier,
    recordingMethodMapper: RecordingMethodMapper = Di.recordingMethodMapper,
    viewModel: MetadataEditorViewModel = viewModel(
        modelClass = MetadataEditorViewModel::class,
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(MetadataEditorViewModel.Companion.METADATA_ENTITY_KEY, metadataEditorModel)
        }
    ),
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        SelectorComponent(
            title = "Recording Method",
            supportText = "Client supplied data recording method to help to understand how the data was recorded",
            selectedText = recordingMethodMapper.map(viewModel.state.recordingMethod),
            items = recordingMethodMapper.recordingMethods,
            itemComposable = { (_, name) ->
                Text(text = name)
            },
            onItemSelected = { (type, _) ->
                viewModel.onEvent(Event.OnRecordingMethodSelected(type))
            }
        )

        OutlinedTextField(
            value = viewModel.state.id,
            enabled = false,
            singleLine = true,
            onValueChange = {},
            label = {
                Text("Id")
            },
            supportingText = {
                Text("Unique identifier of this data, assigned by Health Connect at insertion time. When [Record] is created before insertion, this takes a sentinel value, any assigned value will be ignored.")
            },
            modifier = Modifier.fillMaxWidth()
        )

        //TODO show human readable app name
        OutlinedTextField(
            value = viewModel.state.dataOriginPackageName,
            enabled = false,
            singleLine = true,
            onValueChange = {},
            label = {
                Text("Data origin")
            },
            supportingText = {
                Text("Where the data comes from, such as application information originally generated this data. When [Record] is created before insertion, this contains a sentinel value, any assigned value will be ignored. After insertion, this will be populated with inserted application.")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.state.lastModifiedTime.toString(),
            enabled = false,
            singleLine = true,
            onValueChange = {},
            label = {
                Text("Last modified time")
            },
            supportingText = {
                Text("Automatically populated to when data was last modified (or originally created). When [Record] is created before inserted, this contains a sentinel value, any assigned value will be ignored.")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.state.clientRecordId,
            singleLine = true,
            onValueChange = {
                viewModel.onEvent(Event.OnClientRecordIdChanged(it))
            },
            label = {
                Text("Client record Id")
            },
            supportingText = {
                Column {
                    Text("Optional client supplied record unique data identifier associated with the data.")
                    Text("There is guaranteed a single entry for any type of data with same client provided identifier for a given client. Any new insertions with the same client provided identifier will either replace or be ignored depending on associated [clientRecordVersion].")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.state.clientRecordVersion,
            isError = !viewModel.state.isValid(),
            onValueChange = {
                viewModel.onEvent(Event.OnClientVersionChanged(it))
            },
            label = {
                Text("Client record version")
            },
            supportingText = {
                Column {
                    Text("Optional client supplied version associated with the data.")
                    Text("This determines conflict resolution outcome when there are multiple insertions of the same [clientRecordId]. Data with the highest [clientRecordVersion] takes precedence. [clientRecordVersion] starts with 0.")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        val commonModifier = modifier
            .fillMaxWidth()
            .padding(16.dp)

        when (val model = viewModel.state.deviceEditorModel) {
            //TODO create animation between these states changes
            DeviceEditorModel.Empty -> Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = commonModifier
            ) {
                Text("Device is not specified")
                Button(onClick = {
                    viewModel.onEvent(Event.OnSpecifyDevice)
                }) {
                    Text("Specify")
                }
            }

            is DeviceEditorModel.Specified -> DeviceEditorComponent(
                specifiedDeviceEditorModel = model,
                modifier = commonModifier,
                onTypeItemSelected = { (type, _) -> viewModel.onEvent(Event.OnTypeSelected(type)) },
                onManufacturerValueChanged = { viewModel.onEvent(Event.OnManufacturerChanged(it)) },
                onModelValueChanged = { viewModel.onEvent(Event.OnModelChanged(it)) },
                onRemoveDeviceClicked = { viewModel.onEvent(Event.OnRemoveDevice) },
            )
        }
    }
}

@Composable
@Preview(showBackground = true, heightDp = 1050)
fun MetadataEditorComponentEmptyDevicePreview() {
    val sampleMetadataEditorModel = MetadataEditorModel(
        recordingMethod = 1, // Example value
        id = "sample-id",
        dataOriginPackageName = "com.example.app",
        lastModifiedTime = Instant.now(),
        clientRecordId = "client-record-id-123",
        clientRecordVersion = "1",
        deviceEditorModel = DeviceEditorModel.Empty
    )

    MetadataEditorComponent(
        metadataEditorModel = sampleMetadataEditorModel,
    )
}

@Composable
@Preview(showBackground = true, heightDp = 1300)
fun MetadataEditorComponentSpecifiedDevicePreview() {
    val sampleMetadataEditorModel = MetadataEditorModel(
        recordingMethod = 1, // Example value
        id = "sample-id",
        dataOriginPackageName = "com.example.app",
        lastModifiedTime = Instant.now(),
        clientRecordId = "client-record-id-123",
        clientRecordVersion = "1",
        deviceEditorModel = DeviceEditorModel.Specified(
            type = 1, // Example device type
            manufacturer = "Example Manufacturer",
            model = "Example Model"
        )
    )

    MetadataEditorComponent(
        metadataEditorModel = sampleMetadataEditorModel,
    )
}