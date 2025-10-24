package com.example.healthconnect.components.impl.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.model.DoubleValueEditorModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.DoubleValueEditorComponentViewModel.Event


@Composable
internal fun DoubleValueEditorComponent(
    editorModel: DoubleValueEditorModel,
    onChanged: (DoubleValueEditorModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DoubleValueEditorComponentViewModel = viewModel(
        key = editorModel.type.toString(),
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(DoubleValueEditorComponentViewModel.MODEL_KEY, editorModel)
        }
    ),
) {
    LaunchedEffect(viewModel.state) {
        Log.d(this::class.simpleName, "${editorModel.type.label}: ${viewModel.state}")
        onChanged(viewModel.state)
    }

    OutlinedTextField(
        value = viewModel.state.value,
        suffix = {
            Text(editorModel.type.label) //TODO create more sophisticated view that will support other units
        },
        enabled = true,
        singleLine = true,
        isError = viewModel.state is DoubleValueEditorModel.Invalid,
        onValueChange = {
            viewModel.onEvent(Event.OnValueChanged(it))
        },
        label = {
            Text(editorModel.type.label)
        },
        supportingText = {
            Text(editorModel.type.supportingText)
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
@Preview(showBackground = true)
fun TemperatureEditorComponentValidPreview() {
    DoubleValueEditorComponent(
        editorModel = DoubleValueEditorModel.Valid(
            parsedValue = 123.0,
            type = DoubleValueEditorModel.Type.Temperature(),
        ),
        onChanged = {},
    )
}

@Composable
@Preview(showBackground = true)
fun TemperatureEditorComponentInvalidPreview() {
    DoubleValueEditorComponent(
        editorModel = DoubleValueEditorModel.Invalid(
            value = "231ed",
            type = DoubleValueEditorModel.Type.Temperature(),
        ),
        onChanged = {},
    )
}