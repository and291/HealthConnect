package com.example.healthconnect.permissions.api.domain.framework

/** Describes a permission request submitted to [com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionCoordinator]. */
sealed class PermissionRequest {

    /** Request a single permission. */
    data class Single(val permission: HealthPermission) : PermissionRequest()

    /**
     * Request multiple permissions at once.
     * Must contain at least one element.
     */
    data class Multiple(val permissions: Set<HealthPermission>) : PermissionRequest()

    /** Returns all [HealthPermission]s contained in this request. */
    fun toSet(): Set<HealthPermission> = when (this) {
        is Single   -> setOf(permission)
        is Multiple -> permissions
    }
}
