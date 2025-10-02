package com.example.healthconnect.ui.screen.record.metadata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.record.metadata.MetadataEditorViewModel.Event
import com.example.healthconnect.ui.screen.record.metadata.MetadataEditorViewModel.MetadataModel
import com.example.healthconnect.ui.screen.record.component.SelectorComponent
import java.time.Instant

@Composable
fun MetadataEditorComponent(
    metadataModel: MetadataModel,
    onMetaModelChange: (MetadataModel) -> Unit,
    modifier: Modifier = Modifier,
    recordingMethodMapper: RecordingMethodMapper = Di.recordingMethodMapper,
    viewModel: MetadataEditorViewModel = viewModel(
        modelClass = MetadataEditorViewModel::class,
        factory = Di.editorViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(MetadataEditorViewModel.METADATA_MODEL_KEY, metadataModel)
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
            value = viewModel.state.clientRecordVersion.toString(),
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
    }
}

@Composable
@Preview(showBackground = true, heightDp = 1500)
fun MetadataEditorComponentPreview() {
    val sampleMetadataModel = MetadataModel(
        recordingMethod = 1, // Example value
        id = "sample-id",
        dataOriginPackageName = "com.example.app",
        lastModifiedTime = Instant.now(),
        clientRecordId = "client-record-id-123",
        clientRecordVersion = 1L,
    )

    MetadataEditorComponent(
        metadataModel = sampleMetadataModel,
        onMetaModelChange = { updatedMetadataModel ->
            // Handle metadata changes here
        },
    )
}