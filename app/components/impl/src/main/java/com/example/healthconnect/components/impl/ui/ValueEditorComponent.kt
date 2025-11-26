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
import com.example.healthconnect.components.api.ui.model.ValueComponentModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.ValueEditorComponentViewModel.Event


@Composable
internal fun ValueEditorComponent(
    model: ValueComponentModel,
    onChanged: (ValueComponentModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ValueEditorComponentViewModel = viewModel(
        key = model.type.toString(),
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(ValueEditorComponentViewModel.MODEL_KEY, model)
        }
    ),
) {
    LaunchedEffect(viewModel.state) {
        Log.d(this::class.simpleName, "${model.type.label}: ${viewModel.state}")
        onChanged(viewModel.state)
    }

    OutlinedTextField(
        value = viewModel.state.value,
        suffix = {
            Text(model.type.suffix) //TODO create more sophisticated view that will support other units
        },
        enabled = true,
        singleLine = true,
        isError = !viewModel.state.isValid(),
        onValueChange = {
            viewModel.onEvent(Event.OnValueChanged(it))
        },
        label = {
            Text(model.type.label)
        },
        supportingText = {
            Text(model.type.supportingText)
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
@Preview(showBackground = true)
fun ValueEditorComponentValidPreview() {
    ValueEditorComponent(
        model = ValueComponentModel.Dbl(
            parsedValue = 123.0,
            type = ValueComponentModel.Type.Temperature(),
        ),
        onChanged = {},
    )
}

@Composable
@Preview(showBackground = true)
fun ValueEditorComponentInvalidPreview() {
    ValueEditorComponent(
        model = ValueComponentModel.Dbl(
            value = "231ed",
            type = ValueComponentModel.Type.Temperature(),
        ),
        onChanged = {},
    )
}