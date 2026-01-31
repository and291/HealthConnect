package com.example.healthconnect.components.api.ui.model

import java.util.UUID

sealed class ComponentModel(
    open val presentationId: UUID
) {

    abstract fun isValid(): Boolean
}
