package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.ComponentModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import java.util.UUID

data class PlannedExerciseBlockField(
    val description: StringField,
    val steps: ListField<PlannedExerciseStepField>,
    val repetitions: ValueField.Lng,
    override val instanceId: UUID = UUID.randomUUID(),
) : Composite(instanceId) {

    override fun isValid(): Boolean = description.isValid() &&
            steps.isValid() &&
            repetitions.isValid() &&
            (repetitions.parsedValue ?: 0L) >= 1L &&
            steps.items.isNotEmpty()

    override fun containsInstanceId(instanceId: UUID): Boolean = when (instanceId) {
        description.instanceId, steps.instanceId, repetitions.instanceId -> true
        else -> steps.items.any { it.instanceId == instanceId }
    }

    @Suppress("UNCHECKED_CAST")
    override fun updateFieldByInstanceId(
        instanceId: UUID,
        newField: ComponentModel,
    ): ComponentModel = when (instanceId) {
        description.instanceId -> copy(description = newField as StringField)
        steps.instanceId -> copy(steps = newField as ListField<PlannedExerciseStepField>)
        repetitions.instanceId -> copy(repetitions = newField as ValueField.Lng)
        else -> if (steps.items.any { it.instanceId == instanceId }) {
            val indexToReplace = steps.items.indexOfFirst { it.instanceId == instanceId }
            val mutableList = steps.items.toMutableList()
            mutableList[indexToReplace] = (newField as PlannedExerciseStepField)
            this.copy(steps = steps.copy(items = mutableList.toList()))
        } else {
            this
        }
    }
}