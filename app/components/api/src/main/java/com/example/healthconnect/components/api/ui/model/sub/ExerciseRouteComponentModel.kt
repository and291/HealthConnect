package com.example.healthconnect.components.api.ui.model.sub

import com.example.healthconnect.components.api.ui.model.ComponentModel
import java.time.Instant

data class ExerciseRouteComponentModel(
    val time: Instant,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val horizontalAccuracy: Double?,
    val verticalAccuracy: Double?,
) : ComponentModel {
    override fun isValid(): Boolean = true
}
