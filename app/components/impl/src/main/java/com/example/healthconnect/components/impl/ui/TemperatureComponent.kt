package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.model.TemperatureModel
import com.example.healthconnect.components.impl.di.Di
import com.example.healthconnect.components.impl.ui.TemperatureComponentViewModel.Event


@Composable
fun TemperatureComponent(
    temperatureModel: TemperatureModel,
    modifier: Modifier = Modifier,
    viewModel: TemperatureComponentViewModel = viewModel(
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(TemperatureComponentViewModel.TEMPERATURE_MODEL_KEY, temperatureModel)
        }
    )
) = OutlinedTextField(
    value = viewModel.state.value,
    suffix = {
        Text("Celsius") //TODO create more sophisticated view that will allow editing fahrenheit also
    },
    enabled = true,
    singleLine = true,
    isError = viewModel.state is TemperatureModel.Invalid,
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
fun TemperatureComponentValidPreview() {
    TemperatureComponent(
        temperatureModel = TemperatureModel.Valid(123.0)
    )
}

@Composable
@Preview(showBackground = true)
fun TemperatureComponentInvalidPreview() {
    TemperatureComponent(
        temperatureModel = TemperatureModel.Invalid("231ed")
    )
}