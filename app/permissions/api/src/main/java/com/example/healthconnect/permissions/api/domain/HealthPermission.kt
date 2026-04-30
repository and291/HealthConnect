package com.example.healthconnect.permissions.api.domain

/**
 * Represents a single Health Connect permission.
 *
 * @param permissionString The full Android permission string,
 *   e.g. "android.permission.health.READ_STEPS".
 * @param type Whether this permission grants read or write access.
 */
data class HealthPermission(
    val permissionString: String,
    val type: PermissionType,
) {
    /** Human-readable data-type name stripped of the READ_/WRITE_ prefix, e.g. "STEPS". */
    val dataTypeName: String
        get() = permissionString
            .removePrefix("android.permission.health.READ_")
            .removePrefix("android.permission.health.WRITE_")
}
