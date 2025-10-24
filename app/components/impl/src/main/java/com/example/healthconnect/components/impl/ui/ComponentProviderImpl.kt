package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.api.ui.model.MetadataEditorModel
import com.example.healthconnect.components.api.ui.model.SelectorEditorModel
import com.example.healthconnect.components.api.ui.model.TimeEditorModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorComponent
import com.example.healthconnect.components.impl.ui.metadata.MetadataEditorViewModel
import com.example.healthconnect.components.impl.ui.model.TimeEditorInternalModel
import java.time.Instant
import java.time.ZoneOffset

internal class ComponentProviderImpl : ComponentProvider {

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
                    TimeEditorComponentViewModel.Companion.TIME_MODEL_KEY,
                    TimeEditorInternalModel.create(
                        instant = time,
                        zoneOffset = zoneOffset,
                    )
                )
            }
        )

        LaunchedEffect(timeEditorComponentViewModel.state) {
            Log.d(this::class.simpleName, "Time: ${timeEditorComponentViewModel.state}")
            val timeEditorModel = when (val t = timeEditorComponentViewModel.state.timeModel) {
                is TimeEditorInternalModel.TimeModel.Invalid -> TimeEditorModel.Invalid
                is TimeEditorInternalModel.TimeModel.Valid -> TimeEditorModel.Valid(
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
    override fun DoubleValueEditor(
        editorModel: DoubleValueEditorModel,
        onChanged: (DoubleValueEditorModel) -> Unit
    ) {
        val viewModel: DoubleValueEditorComponentViewModel = viewModel(
            factory = Di.componentViewModelFactory,
            extras = MutableCreationExtras().apply {
                set(DoubleValueEditorComponentViewModel.MODEL_KEY, editorModel)
            }
        )

        LaunchedEffect(viewModel.state) {
            Log.d(this::class.simpleName, "${editorModel.type.label}: ${viewModel.state}")
            onChanged(viewModel.state)
        }
        DoubleValueEditorComponent(
            editorModel = editorModel,
            viewModel = viewModel,
        )
    }

    @Composable
    override fun Selector(
        editor: SelectorEditorModel,
        onLocationChanged: (SelectorEditorModel) -> Unit
    ): Unit = SelectorComponent(
        editor = editor,
        onItemSelected = { onLocationChanged(it) }
    )
}