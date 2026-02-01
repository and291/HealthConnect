package com.example.healthconnect.components.api.ui.model.top

import java.util.UUID

data class StringComponentModel(
    val value: String,
    val type: Type,
    val readOnly: Boolean = false,
    override val presentationId: UUID = UUID.randomUUID(),
) : TopLevelComponentModel(presentationId) {

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
    }
}