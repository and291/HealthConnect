package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.ComponentModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseCompletionGoalField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExercisePerformanceTargetField
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import java.util.UUID

data class PlannedExerciseStepField(
    val exerciseType: SelectorField,
    val exercisePhase: SelectorField,
    val description: StringField,
    val completionGoal: ExerciseCompletionGoalField,
    val performanceTargets: ListField<ExercisePerformanceTargetField>,
    override val instanceId: UUID = UUID.randomUUID(),
) : Composite(instanceId) {

    override fun isValid(): Boolean = exerciseType.isValid() &&
            exercisePhase.isValid() &&
            description.isValid() &&
            completionGoal.isValid() &&
            performanceTargets.isValid()

    override fun containsInstanceId(instanceId: UUID): Boolean = when (instanceId) {
        exerciseType.instanceId, exercisePhase.instanceId, description.instanceId,
        completionGoal.instanceId, performanceTargets.instanceId,
            -> true

        else -> performanceTargets.items.any { it.instanceId == instanceId }
    }

    @Suppress("UNCHECKED_CAST")
    override fun updateFieldByInstanceId(
        instanceId: UUID,
        newField: ComponentModel,
    ): ComponentModel = when (instanceId) {
        exerciseType.instanceId -> copy(exerciseType = newField as SelectorField)
        exercisePhase.instanceId -> copy(exercisePhase = newField as SelectorField)
        description.instanceId -> copy(description = newField as StringField)
        completionGoal.instanceId -> copy(completionGoal = newField as ExerciseCompletionGoalField)
        performanceTargets.instanceId -> copy(performanceTargets = newField as ListField<ExercisePerformanceTargetField>)
        else -> if (performanceTargets.items.any { it.instanceId == instanceId }) {
            val index = performanceTargets.items.indexOfFirst { it.instanceId == instanceId }
            val mutableList = performanceTargets.items.toMutableList()
            mutableList[index] = (newField as ExercisePerformanceTargetField)
            this.copy(performanceTargets = performanceTargets.copy(items = mutableList.toList()))
        } else {
            this
        }
    }
}