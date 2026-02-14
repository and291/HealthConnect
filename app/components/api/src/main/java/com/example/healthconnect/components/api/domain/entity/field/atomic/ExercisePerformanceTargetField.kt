package com.example.healthconnect.components.api.domain.entity.field.atomic

import java.util.UUID

sealed class ExercisePerformanceTargetField(
    override val instanceId: UUID = UUID.randomUUID()
) : Atomic(instanceId) {
    data class HeartRate(
        val minBpm: Double,
        val maxBpm: Double,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExercisePerformanceTargetField(instanceId) {
        override fun isValid() = minBpm >= 0 && maxBpm >= minBpm
    }
    data class Power(
        val minWatts: Double,
        val maxWatts: Double,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExercisePerformanceTargetField(instanceId) {
        override fun isValid() = minWatts >= 0 && maxWatts >= minWatts
    }
    data class Speed(
        val minMetersPerSecond: Double,
        val maxMetersPerSecond: Double,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExercisePerformanceTargetField(instanceId) {
        override fun isValid() = minMetersPerSecond >= 0 && maxMetersPerSecond >= minMetersPerSecond
    }
    data class Cadence(
        val minRpm: Double,
        val maxRpm: Double,
        override val instanceId: UUID = UUID.randomUUID(),
    ) : ExercisePerformanceTargetField(instanceId) {
        override fun isValid() = minRpm >= 0 && maxRpm >= minRpm
    }
    data class Weight(
        val massKg: Double,
        override val instanceId: UUID = UUID.randomUUID(),
        ) : ExercisePerformanceTargetField(instanceId) {
        override fun isValid() = massKg >= 0
    }
}