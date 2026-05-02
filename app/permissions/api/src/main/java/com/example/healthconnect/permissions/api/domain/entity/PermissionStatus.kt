package com.example.healthconnect.permissions.api.domain.entity

import com.example.healthconnect.permissions.api.domain.framework.HealthPermission

/**
 * The current grant status of a single [HealthPermission].
 * Used by the Permissions Overview screen to display per-permission state.
 */
data class PermissionStatus(
    val permission: HealthPermission,
    val isGranted: Boolean,
)