package com.example.healthconnect.permission_overview.entity

/**
 * The current grant status of a single [HealthPermission].
 * Used by the Permissions Overview screen to display per-permission state.
 */
internal data class PermissionStatus(
    val permissionString: String,
    val isGranted: Boolean,
)