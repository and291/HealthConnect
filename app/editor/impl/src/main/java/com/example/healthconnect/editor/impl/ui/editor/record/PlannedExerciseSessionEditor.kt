package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.ExerciseCompletionGoal
import androidx.health.connect.client.records.ExercisePerformanceTarget
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.PlannedExerciseBlock
import androidx.health.connect.client.records.PlannedExerciseSessionRecord
import androidx.health.connect.client.records.PlannedExerciseStep
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.Velocity
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseCompletionGoalField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExercisePerformanceTargetField
import com.example.healthconnect.components.api.domain.entity.field.composite.ListField
import com.example.healthconnect.components.api.domain.entity.field.composite.PlannedExerciseBlockField
import com.example.healthconnect.components.api.domain.entity.field.composite.PlannedExerciseStepField
import com.example.healthconnect.editor.api.domain.record.PlannedExerciseSession
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class PlannedExerciseSessionEditor() : Editor<PlannedExerciseSessionRecord, PlannedExerciseSession>() {

    override fun toModel(
        record: PlannedExerciseSessionRecord,
        mapper: MetadataMapper,
    ): PlannedExerciseSession = PlannedExerciseSession(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset,
            priority = 0
        ),
        metadata = mapper.toEntity(record.metadata),
        title = StringField(
            value = record.title ?: "",
            type = StringField.Type.PlannedExerciseSessionTitle(),
            priority = 2
        ),
        notes = StringField(
            value = record.notes ?: "",
            type = StringField.Type.PlannedExerciseSessionNotes(),
            priority = 3
        ),
        exerciseType = SelectorField(
            value = record.exerciseType,
            type = SelectorField.Type.ExerciseType(),
            priority = 1
        ),
        blocks = ListField(
            items = record.blocks.map { block ->
                PlannedExerciseBlockField(
                    description = StringField(
                        value = block.description ?: "",
                        type = StringField.Type.PlannedExerciseBlockDescription()
                    ),
                    repetitions = ValueField.Lng(
                        parsedValue = block.repetitions.toLong(),
                        type = ValueField.Type.PlannedExerciseBlockRepetitions()
                    ),
                    steps = ListField(
                        items = block.steps.map { step ->
                            PlannedExerciseStepField(
                                exerciseType = SelectorField(
                                    value = step.exerciseType,
                                    type = SelectorField.Type.ExerciseType()
                                ),
                                exercisePhase = SelectorField(
                                    value = step.exercisePhase,
                                    type = SelectorField.Type.ExercisePhase()
                                ),
                                description = StringField(
                                    value = step.description ?: "",
                                    type = StringField.Type.PlannedExerciseStepDescription()
                                ),
                                completionGoal = step.completionGoal.toModel(),
                                performanceTargets = ListField(
                                    items = step.performanceTargets.map {
                                        it.toModel()
                                    },
                                    type = ListField.Type.ExercisePerformanceTargets
                                )
                            )
                        },
                        type = ListField.Type.PlannedExerciseSteps
                    )
                )
            },
            type = ListField.Type.PlannedExerciseBlocks,
            priority = 4
        )
    )

    override fun toRecord(
        validModel: PlannedExerciseSession,
        mapper: MetadataMapper,
    ): PlannedExerciseSessionRecord = PlannedExerciseSessionRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        exerciseType = validModel.exerciseType.value,
        title = validModel.title.value.ifBlank { null },
        notes = validModel.notes.value.ifBlank { null },
        blocks = validModel.blocks.items.map { blockField ->
            PlannedExerciseBlock(
                description = blockField.description.value.ifBlank { null },
                repetitions = blockField.repetitions.parsedValue?.toInt() ?: 1,
                steps = blockField.steps.items.map { stepField ->
                    PlannedExerciseStep(
                        exerciseType = stepField.exerciseType.value,
                        exercisePhase = stepField.exercisePhase.value,
                        description = stepField.description.value.ifBlank { null },
                        completionGoal = stepField.completionGoal.toLib(),
                        performanceTargets = stepField.performanceTargets.items.map { it.toLib() }
                    )
                }
            )
        }
    )

    override fun createDefault(): PlannedExerciseSessionRecord {
        val instant = Instant.now()
        return PlannedExerciseSessionRecord(
            startTime = instant,
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant.plusSeconds(3600),
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            exerciseType = ExerciseSessionRecord.EXERCISE_TYPE_RUNNING,
            blocks = emptyList()
        )
    }

    private fun ExerciseCompletionGoal.toModel(): ExerciseCompletionGoalField = when (this) {
        is ExerciseCompletionGoal.DistanceGoal -> ExerciseCompletionGoalField.Distance(distance.inMeters)
        is ExerciseCompletionGoal.StepsGoal -> ExerciseCompletionGoalField.Steps(steps)
        is ExerciseCompletionGoal.DurationGoal -> ExerciseCompletionGoalField.Duration(duration.seconds)
        is ExerciseCompletionGoal.RepetitionsGoal -> ExerciseCompletionGoalField.Repetitions(repetitions)
        is ExerciseCompletionGoal.ActiveCaloriesBurnedGoal -> ExerciseCompletionGoalField.ActiveCaloriesBurned(activeCalories.inKilocalories)
        is ExerciseCompletionGoal.TotalCaloriesBurnedGoal -> ExerciseCompletionGoalField.TotalEnergyBurned(totalCalories.inKilocalories)
        is ExerciseCompletionGoal.ManualCompletion -> ExerciseCompletionGoalField.ManualCompletion()
        else -> ExerciseCompletionGoalField.ManualCompletion()
    }

    private fun ExerciseCompletionGoalField.toLib(): ExerciseCompletionGoal = when (this) {
        is ExerciseCompletionGoalField.Distance -> ExerciseCompletionGoal.DistanceGoal(Length.meters(meters))
        is ExerciseCompletionGoalField.Steps -> ExerciseCompletionGoal.StepsGoal(count)
        is ExerciseCompletionGoalField.Duration -> ExerciseCompletionGoal.DurationGoal(Duration.ofSeconds(seconds))
        is ExerciseCompletionGoalField.Repetitions -> ExerciseCompletionGoal.RepetitionsGoal(count)
        is ExerciseCompletionGoalField.ActiveCaloriesBurned -> ExerciseCompletionGoal.ActiveCaloriesBurnedGoal(Energy.kilocalories(kilocalories))
        is ExerciseCompletionGoalField.TotalEnergyBurned -> ExerciseCompletionGoal.TotalCaloriesBurnedGoal(Energy.kilocalories(kilocalories))
        is ExerciseCompletionGoalField.ManualCompletion -> ExerciseCompletionGoal.ManualCompletion
    }

    private fun ExercisePerformanceTarget.toModel(): ExercisePerformanceTargetField = when (this) {
        is ExercisePerformanceTarget.HeartRateTarget -> ExercisePerformanceTargetField.HeartRate(minHeartRate, maxHeartRate)
        is ExercisePerformanceTarget.PowerTarget -> ExercisePerformanceTargetField.Power(minPower.inWatts, maxPower.inWatts)
        is ExercisePerformanceTarget.SpeedTarget -> ExercisePerformanceTargetField.Speed(minSpeed.inMetersPerSecond, maxSpeed.inMetersPerSecond)
        is ExercisePerformanceTarget.CadenceTarget -> ExercisePerformanceTargetField.Cadence(minCadence, maxCadence)
        is ExercisePerformanceTarget.WeightTarget -> ExercisePerformanceTargetField.Weight(mass.inKilograms)
        else -> throw Error("Unknown performance target type")
    }

    private fun ExercisePerformanceTargetField.toLib(): ExercisePerformanceTarget = when (this) {
        is ExercisePerformanceTargetField.HeartRate -> ExercisePerformanceTarget.HeartRateTarget(minBpm, maxBpm)
        is ExercisePerformanceTargetField.Power -> ExercisePerformanceTarget.PowerTarget(Power.watts(minWatts), Power.watts(maxWatts))
        is ExercisePerformanceTargetField.Speed -> ExercisePerformanceTarget.SpeedTarget(Velocity.metersPerSecond(minMetersPerSecond), Velocity.metersPerSecond(maxMetersPerSecond))
        is ExercisePerformanceTargetField.Cadence -> ExercisePerformanceTarget.CadenceTarget(minRpm, maxRpm)
        is ExercisePerformanceTargetField.Weight -> ExercisePerformanceTarget.WeightTarget(androidx.health.connect.client.units.Mass.kilograms(massKg))
    }
}
