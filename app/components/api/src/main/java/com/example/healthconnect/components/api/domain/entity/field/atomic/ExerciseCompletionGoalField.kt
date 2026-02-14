package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.util.UUID

sealed class ExerciseCompletionGoalField(
    override val instanceId: UUID = UUID.randomUUID(),
) : Atomic(instanceId) {

    data class Distance(
        val meters: Double,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExerciseCompletionGoalField() {
        override fun isValid() = meters >= 0
    }

    data class Duration(
        val seconds: Long,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExerciseCompletionGoalField() {
        override fun isValid() = seconds >= 0
    }

    data class Repetitions(
        val count: Int,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExerciseCompletionGoalField() {
        override fun isValid() = count >= 0
    }

    data class Steps(
        val count: Int,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExerciseCompletionGoalField() {
        override fun isValid() = count >= 0
    }

    data class ActiveCaloriesBurned(
        val kilocalories: Double,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExerciseCompletionGoalField() {
        override fun isValid() = kilocalories >= 0
    }

    data class TotalEnergyBurned(
        val kilocalories: Double,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExerciseCompletionGoalField() {
        override fun isValid() = kilocalories >= 0
    }

    data class ManualCompletion(
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExerciseCompletionGoalField() {
        override fun isValid() = true
    }
}