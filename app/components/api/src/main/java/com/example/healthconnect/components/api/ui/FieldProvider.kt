package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField

interface FieldProvider {

    @Composable
    fun TimeEditor(
        time: TimeField,
        modifier: Modifier,
        onChanged: (TimeField) -> Unit,
    )

    @Composable
    fun ValueEditor(
        value: ValueField,
        modifier: Modifier,
        onChanged: (ValueField) -> Unit,
    )

    @Composable
    fun Selector(
        selector: SelectorField,
        modifier: Modifier,
        onChanged: (SelectorField) -> Unit,
    )

    @Composable
    fun StringEditor(
        value: StringField,
        modifier: Modifier,
        onChanged: (StringField) -> Unit,
    )

    @Composable
    fun DeviceEditor(
        deviceField: DeviceField,
        modifier: Modifier,
        onChanged: (DeviceField) -> Unit,
    )

    @Composable
    fun ExerciseLapItem(
        item: ExerciseLapField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExerciseLapField) -> Unit,
    )

    @Composable
    fun ExerciseSegmentItem(
        item: ExerciseSegmentField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExerciseSegmentField) -> Unit,
    )

    @Composable
    fun ExerciseRouteLocationItem(
        item: ExerciseRouteField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExerciseRouteField) -> Unit,
    )

    @Composable
    fun RouteResultStatus(type: ListField.Type)
}
