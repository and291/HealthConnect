package com.example.healthconnect.permission_overview.api.domain.entity

import androidx.annotation.StringRes

sealed interface PermissionEntry {
    @get:StringRes
    val nameResId: Int
}

data class ReadWrite(
    @param:StringRes
    override val nameResId: Int,
    val read: Permission,
    val write: Permission,
) : PermissionEntry

data class Single(
    @param:StringRes
    override val nameResId: Int,
    val read: Permission,
) : PermissionEntry