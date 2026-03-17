package com.example.healthconnect.components.api.domain.entity.field.atomic

import com.example.healthconnect.components.api.domain.entity.Field
import java.util.UUID

sealed class Atomic(
    override val instanceId: UUID
) : Field


