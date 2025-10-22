package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthconnect.components.api.ui.model.BloodGlucoseLevelEditorModel
import com.example.healthconnect.components.impl.di.Di

@Composable
internal fun BloodGlucoseLevelEditorComponent(
    editorModel: BloodGlucoseLevelEditorModel,
    modifier: Modifier = Modifier,
    viewModel: BloodGlucoseLevelEditorComponentViewModel = viewModel(
        factory = Di.componentViewModelFactory,
        extras = MutableCreationExtras().apply {
            set(BloodGlucoseLevelEditorComponentViewModel.MODEL_KEY, editorModel)
        }
    )
) = OutlinedTextField(
    value = viewModel.state.value,
    suffix = {
        Text("millimoles per liter") //TODO create more sophisticated view that will allow editing other types
    },
    enabled = true,
    singleLine = true,
    isError = viewModel.state is BloodGlucoseLevelEditorModel.Invalid,
    onValueChange = {
        viewModel.onEvent(BloodGlucoseLevelEditorComponentViewModel.Event.OnLevelChanged(it))
    },
    label = {
        Text("Blood glucose")
    },
    supportingText = {
        Text("Blood glucose level or concentration. Required field. Valid range: 0-50 mmol/L.")
    },
    modifier = modifier.fillMaxWidth()
)

@Composable
@Preview(showBackground = true)
fun BloodGlucoseLevelEditorComponentValidPreview() {
    BloodGlucoseLevelEditorComponent(
        editorModel = BloodGlucoseLevelEditorModel.Valid(123.0)
    )
}

@Composable
@Preview(showBackground = true)
fun BloodGlucoseLevelEditorComponentInvalidPreview() {
    BloodGlucoseLevelEditorComponent(
        editorModel = BloodGlucoseLevelEditorModel.Invalid("231ed")
    )
}