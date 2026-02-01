package com.example.healthconnect.components.api.ui.model.sub

import com.example.healthconnect.components.api.ui.model.ComponentModel
import java.time.Instant

data class ExerciseLapComponentModel(
    val startTime: Instant,
    val endTime: Instant,
    val lengthInMeters: Double?,
) : ComponentModel {
    override fun isValid(): Boolean =
        startTime.isBefore(endTime) && (lengthInMeters == null || lengthInMeters in 0.0..1000000.0)
}