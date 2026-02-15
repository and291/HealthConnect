package com.example.healthconnect.components.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.metadata.Device
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseCompletionGoalField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExercisePerformanceTargetField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SkinTemperatureDeltaField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SleepSessionStageField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.ui.FieldProvider
import com.example.healthconnect.components.impl.ui.editor.atomic.DeviceFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.ExerciseCompletionGoalFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.ExerciseLapFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.ExercisePerformanceTargetFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.ExerciseRouteFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.ExerciseSegmentFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.SelectorFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.SkinTemperatureDeltaFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.SleepSessionStageFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.StringFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.TimeFieldEditor
import com.example.healthconnect.components.impl.ui.editor.atomic.ValueFieldEditor

internal class FieldProviderImpl : FieldProvider {

    @Composable
    override fun TimeEditor(
        time: TimeField,
        modifier: Modifier,
        onChanged: (TimeField) -> Unit,
    ) = TimeFieldEditor(
        model = time,
        onChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun ValueEditor(
        value: ValueField,
        modifier: Modifier,
        onChanged: (ValueField) -> Unit,
    ) = ValueFieldEditor(
        model = value,
        onChanged = onChanged,
        modifier = modifier,
    )

    @Composable
    override fun Selector(
        selector: SelectorField,
        modifier: Modifier,
        onChanged: (SelectorField) -> Unit,
    ): Unit = SelectorFieldEditor(
        editor = selector,
        onItemSelected = { onChanged(it) }
    )

    @Composable
    override fun StringEditor(
        value: StringField,
        modifier: Modifier,
        onChanged: (StringField) -> Unit,
    ) = StringFieldEditor(
        model = value,
        onChanged = onChanged,
        modifier = modifier
    )

    @Composable
    override fun DeviceEditor(
        deviceField: DeviceField,
        modifier: Modifier,
        onChanged: (DeviceField) -> Unit,
    ) = when (deviceField) {
        is DeviceField.Empty -> Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Device is not specified")
            Button(onClick = {
                val defaultDevice = DeviceField.Specified(
                    type = Device.TYPE_UNKNOWN,
                    manufacturer = "",
                    model = "",
                    instanceId = deviceField.instanceId,
                )
                onChanged(defaultDevice)
            }) {
                Text("Specify")
            }
        }

        is DeviceField.Specified -> DeviceFieldEditor(
            specifiedDeviceFieldComponentModel = deviceField,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onTypeItemSelected = { (type, _) ->
                onChanged(deviceField.copy(type = type))
            },
            onManufacturerValueChanged = {
                onChanged(deviceField.copy(manufacturer = it))
            },
            onModelValueChanged = {
                onChanged(deviceField.copy(model = it))
            },
            onRemoveDeviceClicked = {
                onChanged(DeviceField.Empty(deviceField.instanceId))
            },
        )
    }

    @Composable
    override fun ExerciseLapItem(
        item: ExerciseLapField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExerciseLapField) -> Unit,
    ) = ExerciseLapFieldEditor(
        model = item,
        onChanged = onChanged,
        onDelete = onDelete,
        modifier = modifier
    )

    @Composable
    override fun ExerciseSegmentItem(
        item: ExerciseSegmentField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExerciseSegmentField) -> Unit,
    ) = ExerciseSegmentFieldEditor(
        model = item,
        onChanged = onChanged,
        onDelete = onDelete,
        modifier = modifier
    )

    @Composable
    override fun ExerciseRouteLocationItem(
        item: ExerciseRouteField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExerciseRouteField) -> Unit,
    ) = ExerciseRouteFieldEditor(
        location = item,
        onChanged = onChanged,
        onDelete = onDelete,
        modifier = modifier
    )

    @Composable
    override fun ExerciseCompletionGoal(
        item: ExerciseCompletionGoalField,
        modifier: Modifier,
        onChanged: (ExerciseCompletionGoalField) -> Unit,
    ) = ExerciseCompletionGoalFieldEditor(
        model = item,
        onChanged = onChanged,
        modifier = modifier
    )

    @Composable
    override fun ExercisePerformanceTargetItem(
        item: ExercisePerformanceTargetField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExercisePerformanceTargetField) -> Unit,
    ) = ExercisePerformanceTargetFieldEditor(
        model = item,
        onChanged = onChanged,
        onDelete = onDelete,
        modifier = modifier
    )

    @Composable
    override fun RouteResultStatus(
        type: ListField.Type,
    ) = when (type) {
        ListField.Type.ExerciseRoute.RouteResult.ConsentRequired -> Text(
            text = "Lack of permissions to access route data.",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        ListField.Type.ExerciseRoute.RouteResult.NoData -> Text(
            text = "No data yet. You can add some using the button above.",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        else -> {}
    }

    @Composable
    override fun SkinTemperatureDeltaItem(
        item: SkinTemperatureDeltaField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (SkinTemperatureDeltaField) -> Unit,
    ) = SkinTemperatureDeltaFieldEditor(
        item = item,
        onChanged = onChanged,
        onDelete = onDelete,
        modifier = modifier
    )

    @Composable
    override fun SleepSessionStageItem(
        item: SleepSessionStageField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (SleepSessionStageField) -> Unit,
    ) = SleepSessionStageFieldEditor(
        item = item,
        onChanged = onChanged,
        onDelete = onDelete,
        modifier = modifier
    )
}
