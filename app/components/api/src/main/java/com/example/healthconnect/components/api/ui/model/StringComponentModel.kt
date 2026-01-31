package com.example.healthconnect.components.api.ui.model

import java.util.UUID

data class StringComponentModel(
    val value: String,
    val type: Type,
    override val presentationId: UUID = UUID.randomUUID(),
) : ComponentModel(presentationId) {

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
    }
}