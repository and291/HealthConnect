package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.Field
import com.example.healthconnect.components.api.domain.entity.Field.Companion.PRIORITY_DEFAULT
import com.example.healthconnect.components.api.domain.entity.field.atomic.CyclingPedalingCadenceSampleField
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
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import java.time.Instant
import java.util.UUID

data class ListField<T : Field>(
    val items: List<T>,
    val type: Type,
    val config: Configuration<T> = Configuration.from(type),
    override val instanceId: UUID = UUID.randomUUID(),
    override val priority: Int = PRIORITY_DEFAULT,
) : Composite(instanceId) {

    override fun isValid(): Boolean = items.all { it.isValid() }

    override fun containsInstanceId(instanceId: UUID): Boolean {
        return items.any { it.instanceId == instanceId}
    }

    override fun updateFieldByInstanceId(
        instanceId: UUID,
        newField: Field,
    ): Field {
        val mutableList = items.toMutableList()
        @Suppress("UNCHECKED_CAST")
        mutableList[mutableList.indexOfFirst { it.instanceId == instanceId }] = newField as T
        return copy(
            items = mutableList.toList()
        )
    }

    fun removeItemByPresentationId(presentationId: UUID): ListField<T> {
        val mutableList = items.toMutableList()
        mutableList.removeIf { it.instanceId == presentationId }
        return copy(
            items = mutableList.toList()
        )
    }

    fun findAndRemoveFromList(
        targetId: UUID,
    ): ListField<*>? {
        // 1. Base case: If this specific list contains the ID, remove and return
        if (this.containsInstanceId(targetId)) {
            return this.removeItemByPresentationId(targetId)
        }

        // 2. Recursive case: Search through Composite items within this list
        return this.items
            .filterIsInstance<Composite>()
            .firstNotNullOfOrNull { composite ->
                // Get nested lists from the composite and search them
                composite.getComponents()
                    .filterIsInstance<ListField<*>>()
                    .firstNotNullOfOrNull { innerList ->
                        innerList.findAndRemoveFromList(targetId)
                    }
            }
    }

    sealed class Type {
        data object ExerciseSegments : Type()
        data object ExerciseLaps : Type()
        data object PlannedExerciseBlocks : Type()
        data object PlannedExerciseSteps : Type()
        data object ExercisePerformanceTargets : Type()
        data object SkinTemperatureDeltas : Type()
        data object SleepSessionStages : Type()
        data object HeartRateSamples : Type()
        data object CyclingPedalingCadenceSamples : Type()
        data object PowerSamples : Type()
        data object SpeedSamples : Type()
        data object StepsCadenceSamples : Type()

        data class ExerciseRoute(val result: RouteResult) : Type() {
            sealed class RouteResult {
                data object Data : RouteResult()
                data object ConsentRequired : RouteResult()
                data object NoData : RouteResult()
            }
        }
    }

    sealed class Configuration<T : Field> {
        abstract fun createItem(): T
        abstract val label: String
        open val hasStatusContent: Boolean = false
        open val showAddButton: Boolean = true

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun <T : Field> from(
                type: Type,
            ): Configuration<T> = when (type) {
                is Type.ExerciseSegments -> ExerciseSegmentsConfig() as Configuration<T>
                is Type.ExerciseLaps -> ExerciseLapsConfig() as Configuration<T>
                is Type.ExerciseRoute -> ExerciseRouteConfig(type) as Configuration<T>
                is Type.PlannedExerciseBlocks -> PlannedExerciseBlocksConfig() as Configuration<T>
                is Type.PlannedExerciseSteps -> PlannedExerciseStepsConfig() as Configuration<T>
                is Type.ExercisePerformanceTargets -> ExercisePerformanceTargetsConfig() as Configuration<T>
                is Type.SkinTemperatureDeltas -> SkinTemperatureDeltasConfig() as Configuration<T>
                is Type.SleepSessionStages -> SleepSessionStagesConfig() as Configuration<T>
                is Type.HeartRateSamples -> HeartRateSamplesConfig() as Configuration<T>
                is Type.CyclingPedalingCadenceSamples -> CyclingPedalingCadenceSamplesConfig() as Configuration<T>
                is Type.PowerSamples -> PowerSamplesConfig() as Configuration<T>
                is Type.SpeedSamples -> SpeedSamplesConfig() as Configuration<T>
                is Type.StepsCadenceSamples -> StepsCadenceSamplesConfig() as Configuration<T>
            }
        }

        private class ExerciseSegmentsConfig() :
            Configuration<ExerciseSegmentField>() {
            override val label = "Exercise Segments"
            override fun createItem() = ExerciseSegmentField(
                startTime = Instant.now(),
                endTime = Instant.now().plusSeconds(60),
                segmentType = 0,
                repetitions = 0
            )
        }

        private class ExerciseLapsConfig() : Configuration<ExerciseLapField>() {
            override val label = "Exercise Laps"
            override fun createItem() = ExerciseLapField(
                startTime = Instant.now(),
                endTime = Instant.now().plusSeconds(60),
                lengthInMeters = null
            )
        }

        private class PlannedExerciseBlocksConfig() : Configuration<PlannedExerciseBlockField>() {
            override val label = "Planned Exercise Blocks"
            override fun createItem() = PlannedExerciseBlockField(
                description = StringField(
                    value = "",
                    type = StringField.Type.PlannedExerciseBlockDescription()
                ),
                steps = ListField(
                    items = emptyList(),
                    type = Type.PlannedExerciseSteps
                ),
                repetitions = ValueField.Lng(
                    parsedValue = 1L,
                    type = ValueField.Type.PlannedExerciseBlockRepetitions()
                )
            )
        }

        private class PlannedExerciseStepsConfig() : Configuration<PlannedExerciseStepField>() {
            override val label = "Planned Exercise Steps"
            override fun createItem() = PlannedExerciseStepField(
                exerciseType = SelectorField(
                    value = 0,
                    type = SelectorField.Type.ExerciseType()
                ),
                exercisePhase = SelectorField(
                    value = 0,
                    type = SelectorField.Type.ExercisePhase()
                ),
                description = StringField(
                    value = "",
                    type = StringField.Type.PlannedExerciseStepDescription()
                ),
                completionGoal = ExerciseCompletionGoalField.ManualCompletion(),
                performanceTargets = ListField(
                    items = emptyList(),
                    type = Type.ExercisePerformanceTargets
                )
            )
        }

        private class ExercisePerformanceTargetsConfig() :
            Configuration<ExercisePerformanceTargetField>() {
            override val label = "Performance Targets"
            override fun createItem() = ExercisePerformanceTargetField.HeartRate(0.0,0.0)
        }

        private class ExerciseRouteConfig(
            private val type: Type.ExerciseRoute,
        ) : Configuration<ExerciseRouteField>() {
            override val label = "Exercise Route"
            override val showAddButton =
                type.result != Type.ExerciseRoute.RouteResult.ConsentRequired

            override val hasStatusContent: Boolean
                get() = type.result != Type.ExerciseRoute.RouteResult.Data

            override fun createItem() = ExerciseRouteField(
                time = Instant.now(),
                latitude = 0.0,
                longitude = 0.0,
                altitude = null,
                horizontalAccuracy = null,
                verticalAccuracy = null
            )
        }

        private class SkinTemperatureDeltasConfig() : Configuration<SkinTemperatureDeltaField>() {
            override val label = "Temperature Deltas"
            override fun createItem() = SkinTemperatureDeltaField(
                time = StringField(
                    value = Instant.now().toString(),
                    type = StringField.Type.SkinTemperatureDeltaTime()
                ),
                delta = ValueField.Dbl(
                    parsedValue = 0.0,
                    type = ValueField.Type.TemperatureDelta()
                )
            )
        }

        private class SleepSessionStagesConfig() : Configuration<SleepSessionStageField>() {
            override val label = "Sleep Stages"
            override fun createItem() = SleepSessionStageField(
                startTime = StringField(
                    value = Instant.now().toString(),
                    type = StringField.Type.SleepSessionStageTime("Start Time")
                ),
                endTime = StringField(
                    value = Instant.now().plusSeconds(3600).toString(),
                    type = StringField.Type.SleepSessionStageTime("End Time")
                ),
                stage = 0
            )
        }

        private class HeartRateSamplesConfig() : Configuration<HeartRateSampleField>() {
            override val label = "Heart Rate Samples"
            override fun createItem() = HeartRateSampleField(
                time = Instant.now(),
                heartRate = ValueField.Lng(
                    parsedValue = 70L,
                    type = ValueField.Type.BeatsPerMinute()
                )
            )
        }

        private class CyclingPedalingCadenceSamplesConfig() : Configuration<CyclingPedalingCadenceSampleField>() {
            override val label = "Cycling Pedaling Cadence Samples"
            override fun createItem() = CyclingPedalingCadenceSampleField(
                time = Instant.now(),
                cadence = ValueField.Dbl(
                    parsedValue = 90.0,
                    type = ValueField.Type.CyclingPedalingCadence()
                )
            )
        }

        private class PowerSamplesConfig() : Configuration<PowerSampleField>() {
            override val label = "Power Samples"
            override fun createItem() = PowerSampleField(
                time = Instant.now(),
                power = ValueField.Dbl(
                    parsedValue = 200.0,
                    type = ValueField.Type.PowerWatt()
                )
            )
        }

        private class SpeedSamplesConfig() : Configuration<SpeedSampleField>() {
            override val label = "Speed Samples"
            override fun createItem() = SpeedSampleField(
                time = Instant.now(),
                speed = ValueField.Dbl(
                    parsedValue = 5.0,
                    type = ValueField.Type.Speed()
                )
            )
        }

        private class StepsCadenceSamplesConfig() : Configuration<StepsCadenceSampleField>() {
            override val label = "Steps Cadence Samples"
            override fun createItem() = StepsCadenceSampleField(
                time = Instant.now(),
                cadence = ValueField.Dbl(
                    parsedValue = 120.0,
                    type = ValueField.Type.StepsCadence()
                )
            )
        }
    }
}
