package com.example.healthconnect.components.api.domain.entity

import java.util.UUID

interface ComponentModel {
    val instanceId: UUID

    fun isValid(): Boolean
}