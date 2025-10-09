package com.example.healthconnect.ui.screen.record

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.di.Di
import com.example.healthconnect.ui.screen.record.BasalBodyTemperatureViewModel.Effect
import com.example.healthconnect.ui.screen.component.metadata.MetadataEditorComponent
import com.example.healthconnect.ui.screen.component.metadata.MetadataEditorViewModel
import com.example.healthconnect.ui.screen.component.metadata.mapper.MetadataMapper

@Composable
fun BasalBodyTemperatureScreen(
    record: BasalBodyTemperatureRecord,
    modifier: Modifier = Modifier,
    metadataMapper: MetadataMapper = Di.metadataMapper,
    viewModel: BasalBodyTemperatureViewModel = viewModel(
        modelClass = BasalBodyTemperatureViewModel::class,
        factory = Di.recordViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(BasalBodyTemperatureViewModel.RECORD_KEY, record)
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
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val metadataModel = metadataMapper.toUiModel(record.metadata)
        val metadataViewModel: MetadataEditorViewModel = viewModel(
            modelClass = MetadataEditorViewModel::class,
            key = record.metadata.id, //TODO double-check if this a correct key to use for new viewmodel instance creation
            factory = Di.metadataComponentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(MetadataEditorViewModel.METADATA_MODEL_KEY, metadataModel)
            }
        )

        LaunchedEffect(metadataViewModel.state) {
            Log.d(this::class.simpleName, "Metadata: ${metadataViewModel.state}")
            viewModel.onEvent(BasalBodyTemperatureViewModel.Event.OnMetaModelChanged(metadataViewModel.state))
        }

        Text("Metadata:")
        MetadataEditorComponent(
            metadataModel = metadataModel,
            viewModel = metadataViewModel
        )

        //TODO show the button only if there are changes to save
        Button(onClick = {
            viewModel.onEvent(BasalBodyTemperatureViewModel.Event.OnSave)
        }) {
            Text("Save changes")
        }
    }
}