package com.example.healthconnect.editor.api.domain.model

import com.example.healthconnect.components.api.domain.entity.ComponentModel
import java.util.UUID

sealed class FieldModificationEvent {
    data class OnChanged(
        val component: ComponentModel,
    ) : FieldModificationEvent()

    data class RemoveListItem(
        val instanceId: UUID,
    ) : FieldModificationEvent()
}