package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.model.TemperatureEditorModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.TemperatureEditorComponentViewModel.Event


@Composable
fun TemperatureEditorComponent(
    temperatureEditorModel: TemperatureEditorModel,
    modifier: Modifier = Modifier,
    viewModel: TemperatureEditorComponentViewModel = viewModel(
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(TemperatureEditorComponentViewModel.TEMPERATURE_MODEL_KEY, temperatureEditorModel)
        }
    )
) = OutlinedTextField(
    value = viewModel.state.value,
    suffix = {
        Text("Celsius") //TODO create more sophisticated view that will allow editing fahrenheit also
    },
    enabled = true,
    singleLine = true,
    isError = viewModel.state is TemperatureEditorModel.Invalid,
    onValueChange = {
        viewModel.onEvent(Event.OnTemperatureChanged(it))
    },
    label = {
        Text("Temperature")
    },
    supportingText = {
        Text("Temperature in \"Temperature\" unit.")
    },
    modifier = modifier.fillMaxWidth()
)

@Composable
@Preview(showBackground = true)
fun TemperatureEditorComponentValidPreview() {
    TemperatureEditorComponent(
        temperatureEditorModel = TemperatureEditorModel.Valid(123.0)
    )
}

@Composable
@Preview(showBackground = true)
fun TemperatureEditorComponentInvalidPreview() {
    TemperatureEditorComponent(
        temperatureEditorModel = TemperatureEditorModel.Invalid("231ed")
    )
}