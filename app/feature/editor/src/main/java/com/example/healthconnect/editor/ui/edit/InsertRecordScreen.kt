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
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthconnect.editor.api.ui.ComponentFactory
import com.example.healthconnect.editor.di.Locator
import com.example.healthconnect.editor.ui.edit.InsertRecordViewModel.Event
import com.example.healthconnect.editor.ui.edit.InsertRecordViewModel.State
import kotlin.reflect.KClass


@Composable
internal fun InsertRecordScreen(
    recordClass: KClass<*>,
    modifier: Modifier = Modifier,
    componentFactory: ComponentFactory = Locator.impl.componentFactory,
    viewModel: InsertRecordViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                InsertRecordViewModel(
                    recordClass = recordClass,
                    createEditable = Locator.impl.createEditable,
                    insert = Locator.impl.insert,
                )
            }
        },
        extras = MutableCreationExtras().apply {
            set(InsertRecordViewModel.RECORD_CLASS_KEY, recordClass)
        }
    ),
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(16.dp)
    ) {
        with(componentFactory) {
            create(viewModel.sortedFields) {
                viewModel.onEvent(it)
            }
        }

        item {
            Column {
                when (val state = viewModel.state) {
                    is State.Edition, is State.InsertResult -> Row {
                        Button(
                            enabled = viewModel.state.model.isValid(),
                            onClick = { viewModel.onEvent(Event.OnInsert) }
                        ) {
                            Text("Insert")
                        }
                        if (state is State.InsertResult) {
                            Text("Insert Result: ${state.result}")
                        }
                        if (state is State.Edition && state.errorCreatingEntity != null) {
                            Text("Error creating entity: ${state.errorCreatingEntity}")
                        }
                    }

                    is State.InsertInProgress -> {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
