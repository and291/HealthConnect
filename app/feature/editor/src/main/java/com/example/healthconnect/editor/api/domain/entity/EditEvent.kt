package com.example.healthconnect.editor.api.domain.entity

import java.util.UUID

sealed class EditEvent {

    data class OnChanged(
        val component: EditableField,
    ) : EditEvent()

    data class RemoveListItem(
        val instanceId: UUID,
    ) : EditEvent()
}