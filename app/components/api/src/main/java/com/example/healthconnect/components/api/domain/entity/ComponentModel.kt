package com.example.healthconnect.components.api.domain.entity

import java.util.UUID

interface ComponentModel {
    val instanceId: UUID
    val priority: Int get() = PRIORITY_DEFAULT

    fun isValid(): Boolean

    companion object {
        const val PRIORITY_DEFAULT = Int.MAX_VALUE
    }
}