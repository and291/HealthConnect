package com.example.healthconnect.components.api.domain.entity.field.composite

import com.example.healthconnect.components.api.domain.entity.ComponentModel
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseLapField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseRouteField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ExerciseSegmentField
import java.time.Instant
import java.util.UUID

data class ListField<T : ComponentModel>(
    val items: List<T>,
    val type: Type,
    val config: Configuration<T> = Configuration.from(type),
    override val instanceId: UUID = UUID.randomUUID(),
) : Composite(instanceId) {

    override fun isValid(): Boolean = items.all { it.isValid() }

    fun removeItemByPresentationId(presentationId: UUID): ListField<T> {
        val mutableList = items.toMutableList()
        mutableList.removeIf { it.instanceId == presentationId }
        return copy(
            items = mutableList.toList()
        )
    }

    sealed class Type {
        data object ExerciseSegments : Type()
        data object ExerciseLaps : Type()

        data class ExerciseRoute(val result: RouteResult) : Type() {
            sealed class RouteResult {
                data object Data : RouteResult()
                data object ConsentRequired : RouteResult()
                data object NoData : RouteResult()
            }
        }
    }

    sealed class Configuration<T : ComponentModel> {
        abstract fun createItem(): T
        abstract val label: String
        open val hasStatusContent: Boolean = false
        open val showAddButton: Boolean = true

        companion object {
            @Suppress("UNCHECKED_CAST")
            fun <T : ComponentModel> from(
                type: Type
            ): Configuration<T> = when (type) {
                is Type.ExerciseSegments -> ExerciseSegmentsConfig() as Configuration<T>
                is Type.ExerciseLaps -> ExerciseLapsConfig() as Configuration<T>
                is Type.ExerciseRoute -> ExerciseRouteConfig(type) as Configuration<T>
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
    }
}
