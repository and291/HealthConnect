package com.example.healthconnect.permissions.api.domain

/** The outcome of a permission request, emitted on [com.example.healthconnect.permissions.api.usecase.PermissionCoordinator.results]. */
sealed class PermissionResult {

    /** Every requested permission was granted. */
    data class AllGranted(val granted: Set<HealthPermission>) : PermissionResult()

    /** Some permissions were granted and some were denied. */
    data class SomeGranted(
        val granted: Set<HealthPermission>,
        val denied: Set<HealthPermission>,
    ) : PermissionResult()

    /** Every requested permission was denied. */
    data class AllDenied(val denied: Set<HealthPermission>) : PermissionResult()
}
