package com.example.healthconnect.permissions.api.domain.framework

/**
 * Represents a single Health Connect permission.
 *
 * @param permissionString The full Android permission string,
 *   e.g. "android.permission.health.READ_STEPS".
 */
data class HealthPermission(
    val permissionString: String,
)
