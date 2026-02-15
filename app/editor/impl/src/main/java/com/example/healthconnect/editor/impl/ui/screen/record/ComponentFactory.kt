package com.example.healthconnect.editor.impl.ui.screen.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthconnect.components.api.ui.FieldProvider
import com.example.healthconnect.components.api.domain.entity.ComponentModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.Atomic
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import com.example.healthconnect.components.api.domain.entity.field.composite.PlannedExerciseStepField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SkinTemperatureDeltaField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SleepSessionStageField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.Composite
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseCompletionGoalField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExercisePerformanceTargetField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.components.api.domain.entity.field.composite.PlannedExerciseBlockField
import com.example.healthconnect.editor.api.domain.model.FieldModificationEvent
import com.example.healthconnect.editor.api.domain.model.FieldModificationEvent.*
import com.example.healthconnect.editor.api.domain.record.Model

class ComponentFactory(
    private val provider: FieldProvider,
) {

    fun LazyListScope.create(
        model: Model,
        modifier: Modifier = Modifier,
        eventHandler: (FieldModificationEvent) -> Unit,
    ) = model.getComponents().forEach { componentModel ->
        createByType(componentModel, modifier, eventHandler)
    }

    @Composable
    private fun CreateAtomicComponent(
        componentModel: Atomic,
        modifier: Modifier = Modifier,
        eventHandler: (FieldModificationEvent) -> Unit,
    ) = with(provider) {
        when (componentModel) {
            is TimeField -> TimeEditor(
                time = componentModel,
                modifier = modifier,
            ) { eventHandler(OnChanged(it)) }

            is ValueField -> ValueEditor(
                value = componentModel,
                modifier = modifier
            ) { eventHandler(OnChanged(it)) }

            is SelectorField -> Selector(
                selector = componentModel,
                modifier = modifier
            ) { eventHandler(OnChanged(it)) }

            is StringField -> StringEditor(
                value = componentModel,
                modifier = modifier
            ) { eventHandler(OnChanged(it)) }

            is DeviceField -> DeviceEditor(
                deviceField = componentModel,
                modifier = modifier
            ) { eventHandler(OnChanged(it)) }

            is ExerciseLapField -> ExerciseLapItem(
                item = componentModel,
                modifier = modifier,
                onDelete = { eventHandler(RemoveListItem(componentModel.instanceId)) }
            ) { eventHandler(OnChanged(it)) }

            is ExerciseRouteField -> ExerciseRouteLocationItem(
                item = componentModel,
                modifier = modifier,
                onDelete = { eventHandler(RemoveListItem(componentModel.instanceId)) }
            ) { eventHandler(OnChanged(it)) }

            is ExerciseSegmentField -> ExerciseSegmentItem(
                item = componentModel,
                modifier = modifier,
                onDelete = { eventHandler(RemoveListItem(componentModel.instanceId)) }
            ) { eventHandler(OnChanged(it)) }

            is ExercisePerformanceTargetField -> ExercisePerformanceTargetItem(
                item = componentModel,
                modifier = modifier,
                onDelete = { eventHandler(RemoveListItem(componentModel.instanceId)) }
            ) { eventHandler(OnChanged(it)) }

            is ExerciseCompletionGoalField -> ExerciseCompletionGoal(
                item = componentModel,
                modifier = modifier,
            ) { eventHandler(OnChanged(it)) }

            is SkinTemperatureDeltaField -> SkinTemperatureDeltaItem(
                item = componentModel,
                modifier = modifier,
                onDelete = { eventHandler(RemoveListItem(componentModel.instanceId)) }
            ) { eventHandler(OnChanged(it)) }

            is SleepSessionStageField -> SleepSessionStageItem(
                item = componentModel,
                modifier = modifier,
                onDelete = { eventHandler(RemoveListItem(componentModel.instanceId)) }
            ) { eventHandler(OnChanged(it)) }
        }
    }

    private fun LazyListScope.createComplexComponent(
        model: Composite,
        modifier: Modifier = Modifier,
        eventHandler: (FieldModificationEvent) -> Unit,
    ) {
        when (model) {
            is MetadataField -> {
                item(key = "metadata_header_${model.instanceId}") {
                    Text(
                        text = "Metadata",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                model.getComponents().forEach { model ->
                    createByType(model, modifier, eventHandler)
                }
                item(key = "metadata_divider_${model.instanceId}") {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            is ListField<*> -> {
                item(key = "list_header_${model.instanceId}") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = model.config.label,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (model.config.showAddButton) {
                            Button(onClick = {
                                @Suppress("UNCHECKED_CAST")
                                eventHandler(
                                    OnChanged(
                                        component = (model as ListField<ComponentModel>).copy(
                                            items = model.items + model.config.createItem()
                                        )
                                    )
                                )
                            }) {
                                Text("Add")
                            }
                        }
                    }
                }

                if (model.config.hasStatusContent) {
                    item(key = "list_status_content") {
                        provider.RouteResultStatus(model.type)
                    }
                }

                model.items.forEach { itemModel ->
                    createByType(itemModel, modifier, eventHandler)
                }

                item(key = "list_divider_${model.instanceId}") {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            is PlannedExerciseBlockField -> {
                item(key = "planned_exercise_block_header_${model.instanceId}") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Planned Exercise Block",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(onClick = { eventHandler(RemoveListItem(model.instanceId)) }) {
                            Text("Remove")
                        }
                    }
                }
                model.getComponents().forEach { itemModel ->
                    createByType(itemModel, modifier, eventHandler)
                }
                item(key = "planned_exercise_block_divider_${model.instanceId}") {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            is PlannedExerciseStepField -> {
                item(key = "planned_exercise_step_header_${model.instanceId}") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Planned Exercise Step",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(onClick = { eventHandler(RemoveListItem(model.instanceId)) }) {
                            Text("Remove")
                        }
                    }
                }
                model.getComponents().forEach { itemModel ->
                    createByType(itemModel, modifier, eventHandler)
                }
                item(key = "planned_exercise_step_divider_${model.instanceId}") {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }

    private fun LazyListScope.createByType(
        model: ComponentModel,
        modifier: Modifier,
        eventHandler: (FieldModificationEvent) -> Unit,
    ) = when (model) {
        is Atomic -> item(key = model.instanceId) {
            CreateAtomicComponent(model, modifier, eventHandler)
        }

        is Composite -> createComplexComponent(model, modifier, eventHandler)

        else -> throw IllegalStateException("Unknown component type: $model")
    }
}
