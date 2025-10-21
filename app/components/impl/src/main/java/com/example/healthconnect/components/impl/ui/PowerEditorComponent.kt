package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.model.PowerEditorModel
import com.example.healthconnect.components.impl.di.Di

@Composable
fun PowerEditorComponent(
    powerEditorModel: PowerEditorModel,
    modifier: Modifier = Modifier,
    viewModel: PowerEditorComponentViewModel = viewModel(
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(PowerEditorComponentViewModel.MODEL_KEY, powerEditorModel)
        }
    )
) = OutlinedTextField(
    value = viewModel.state.value,
    suffix = {
        Text("kilocalories per day") //TODO create more sophisticated view that will allow editing watts also
    },
    enabled = true,
    singleLine = true,
    isError = viewModel.state is PowerEditorModel.Invalid,
    onValueChange = {
        viewModel.onEvent(PowerEditorComponentViewModel.Event.OnPowerChanged(it))
    },
    label = {
        Text("Power")
    },
    supportingText = {
        Text("Power in \"Power\" unit.")
    },
    modifier = modifier.fillMaxWidth()
)

@Composable
@Preview(showBackground = true)
fun PowerEditorComponentValidPreview() {
    PowerEditorComponent(
        powerEditorModel = PowerEditorModel.Valid(123.0)
    )
}

@Composable
@Preview(showBackground = true)
fun PowerEditorComponentInvalidPreview() {
    PowerEditorComponent(
        powerEditorModel = PowerEditorModel.Invalid("231ed")
    )
}