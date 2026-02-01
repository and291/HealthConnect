package com.example.healthconnect.components.api.ui.model.top

import com.example.healthconnect.components.api.ui.model.ComponentModel
import java.util.UUID

sealed class TopLevelComponentModel(
    open val presentationId: UUID
) : ComponentModel