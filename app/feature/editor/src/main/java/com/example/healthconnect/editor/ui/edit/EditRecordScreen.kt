package com.example.healthconnect.editor.ui.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthconnect.editor.api.ui.ComponentFactory
import com.example.healthconnect.editor.di.Locator
import com.example.healthconnect.editor.ui.edit.EditRecordViewModel.State
import com.example.healthconnect.editor.ui.edit.EditRecordViewModel.Event
import java.util.UUID
import kotlin.reflect.KClass


@Composable
internal fun EditRecordScreen(
    editableUUID: UUID,
    recordClass: KClass<*>,
    modifier: Modifier = Modifier,
    componentFactory: ComponentFactory = Locator.impl.componentFactory,
    viewModel: EditRecordViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                EditRecordViewModel(
                    editableUUID = editableUUID,
                    kClass = recordClass,
                    getEditable = Locator.impl.getEditable,
                    update = Locator.impl.update,
                )
            }
        }
    ),
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(16.dp)
    ) {
        when (val state = viewModel.state) {
            is State.Loading -> item {
                CircularProgressIndicator()
            }

            is State.Loaded -> {
                with(componentFactory) {
                    create(viewModel.sortedFields) {
                        viewModel.onEvent(it)
                    }
                }

                item {
                    Column {
                        if (viewModel.isChanged) {
                            Text("There is unsaved changes")
                        }

                        when (state) {
                            is State.Edition, is State.UpdateResult -> Row {
                                Button(
                                    enabled = viewModel.isChanged && state.model.isValid(),
                                    onClick = { viewModel.onEvent(Event.OnUpdate(upsert = false)) }
                                ) {
                                    Text("Save")
                                }
                                if (state is State.UpdateResult) {
                                    Text("Update Result: ${state.result}")
                                }
                                if (state is State.Edition && state.errorCreatingEntity != null) {
                                    Text("Error creating entity: ${state.errorCreatingEntity}")
                                }
                            }

                            is State.UpdateInProgress -> {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
