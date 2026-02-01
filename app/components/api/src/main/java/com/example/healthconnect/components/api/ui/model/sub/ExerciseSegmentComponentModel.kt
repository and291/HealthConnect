package com.example.healthconnect.components.api.ui.model.sub

import com.example.healthconnect.components.api.ui.model.ComponentModel
import java.time.Instant

data class ExerciseSegmentComponentModel(
    val startTime: Instant,
    val endTime: Instant,
    val segmentType: Int,
    val repetitions: Int,
) : ComponentModel {
    override fun isValid(): Boolean = startTime.isBefore(endTime) && repetitions >= 0
}