package com.example.healthconnect.components.api.domain.entity.field.atomic

import com.example.healthconnect.components.api.domain.entity.Field.Companion.PRIORITY_DEFAULT
import java.util.UUID

data class StringField(
    val value: String,
    val type: Type,
    val readOnly: Boolean = false,
    override val instanceId: UUID = UUID.randomUUID(),
    override val priority: Int = PRIORITY_DEFAULT,
) : Atomic(instanceId) {

    override fun isValid(): Boolean = true

    sealed class Type {
        abstract val label: String
        abstract val supportingText: String

        data class MindfulnessSessionTitle(
            override val label: String = "Title",
            override val supportingText: String = "Title of the session. Optional field.",
        ) : Type()

        data class MindfulnessSessionNotes(
            override val label: String = "Notes",
            override val supportingText: String = "Notes regarding the session. Optional field.",
        ) : Type()

        data class ExerciseSessionTitle(
            override val label: String = "Title",
            override val supportingText: String = "Title of the exercise session. Optional field.",
        ) : Type()

        data class ExerciseSessionNotes(
            override val label: String = "Notes",
            override val supportingText: String = "Notes regarding the exercise session. Optional field.",
        ) : Type()

        data class ExerciseSessionPlannedExerciseSessionId(
            override val label: String = "Planned exercise session ID",
            override val supportingText: String = "The planned exercise session this workout was based upon. Optional field.",
        ) : Type()

        data class PlannedExerciseSessionTitle(
            override val label: String = "Title",
            override val supportingText: String = "Title of the planned exercise session. Optional field.",
        ) : Type()

        data class PlannedExerciseSessionNotes(
            override val label: String = "Notes",
            override val supportingText: String = "Notes regarding the planned exercise session. Optional field.",
        ) : Type()

        data class PlannedExerciseStepDescription(
            override val label: String = "Description",
            override val supportingText: String = "Description of the planned exercise step. Optional field.",
        ) : Type()

        data class PlannedExerciseBlockDescription(
            override val label: String = "Description",
            override val supportingText: String = "Description of the planned exercise block. Optional field.",
        ) : Type()

        data class MetadataClientRecordId(
            override val label: String = "Client record Id",
            override val supportingText: String = "Optional client supplied record unique data identifier associated with the data.",
        ) : Type()

        data class MetadataClientRecordVersion(
            override val label: String = "Client record version",
            override val supportingText: String = "Optional client supplied version associated with the data.",
        ) : Type()

        data class MetadataId(
            override val label: String = "Id",
            override val supportingText: String = "Unique identifier of this data, assigned by Health Connect at insertion time.",
        ) : Type()

        data class MetadataDataOrigin(
            override val label: String = "Data origin",
            override val supportingText: String = "Where the data comes from, such as application information originally generated this data.",
        ) : Type()

        data class MetadataLastModifiedTime(
            override val label: String = "Last modified time",
            override val supportingText: String = "Automatically populated to when data was last modified (or originally created).",
        ) : Type()

        data class NutritionName(
            override val label: String = "Name",
            override val supportingText: String = "Name of the food or drink. Optional field.",
        ) : Type()

        data class SkinTemperatureDeltaTime(
            override val label: String = "Time",
            override val supportingText: String = "The point in time when the measurement was taken. Format: ISO-8601.",
        ) : Type()

        data class SleepSessionStageTime(
            override val label: String,
            override val supportingText: String = "Start or end time of the sleep stage. Format: ISO-8601.",
        ) : Type()
    }
}
