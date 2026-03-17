package com.example.healthconnect.editor.api.domain.model

import com.example.healthconnect.components.api.domain.entity.Field
import java.util.UUID

sealed class FieldModificationEvent {
    data class OnChanged(
        val component: Field,
    ) : FieldModificationEvent()

    data class RemoveListItem(
        val instanceId: UUID,
    ) : FieldModificationEvent()
}