package com.example.healthconnect.components.api.ui.model.top

import com.example.healthconnect.components.api.ui.model.ComponentModel
import java.util.UUID

data class ListComponentModel<T : ComponentModel>(
    val items: List<T>,
    val type: Type,
    override val presentationId: UUID = UUID.randomUUID(),
) : TopLevelComponentModel(presentationId) {

    override fun isValid(): Boolean = items.all { it.isValid() }

    sealed class Type {
        abstract val label: String
        data object ExerciseSegments : Type() { override val label = "Exercise Segments" }
        data object ExerciseLaps : Type() { override val label = "Exercise Laps" }
        data class ExerciseRoute(val result: RouteResult) : Type() {
            override val label = "Exercise Route"

            sealed class RouteResult {
                data object Data : RouteResult()
                data object ConsentRequired : RouteResult()
                data object NoData : RouteResult()
            }
        }
    }
}
