package com.example.healthconnect.components.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.components.api.domain.entity.field.atomic.CyclingPedalingCadenceSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.DeviceField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseCompletionGoalField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExercisePerformanceTargetField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import com.example.healthconnect.components.api.domain.entity.field.atomic.HeartRateSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.PowerSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SkinTemperatureDeltaField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SleepSessionStageField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SpeedSampleField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StepsCadenceSampleField
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
    fun ExerciseCompletionGoal(
        item: ExerciseCompletionGoalField,
        modifier: Modifier,
        onChanged: (ExerciseCompletionGoalField) -> Unit,
    )

    @Composable
    fun ExercisePerformanceTargetItem(
        item: ExercisePerformanceTargetField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (ExercisePerformanceTargetField) -> Unit,
    )

    @Composable
    fun RouteResultStatus(type: ListField.Type)

    @Composable
    fun SkinTemperatureDeltaItem(
        item: SkinTemperatureDeltaField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (SkinTemperatureDeltaField) -> Unit,
    )

    @Composable
    fun SleepSessionStageItem(
        item: SleepSessionStageField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (SleepSessionStageField) -> Unit,
    )

    @Composable
    fun HeartRateSampleItem(
        item: HeartRateSampleField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (HeartRateSampleField) -> Unit,
    )

    @Composable
    fun CyclingPedalingCadenceSampleItem(
        item: CyclingPedalingCadenceSampleField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (CyclingPedalingCadenceSampleField) -> Unit,
    )

    @Composable
    fun PowerSampleItem(
        item: PowerSampleField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (PowerSampleField) -> Unit,
    )

    @Composable
    fun SpeedSampleItem(
        item: SpeedSampleField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (SpeedSampleField) -> Unit,
    )

    @Composable
    fun StepsCadenceSampleItem(
        item: StepsCadenceSampleField,
        modifier: Modifier,
        onDelete: () -> Unit,
        onChanged: (StepsCadenceSampleField) -> Unit,
    )
}
