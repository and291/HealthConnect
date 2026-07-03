package com.example.healthconnect.permission_overview.api.navigation

import com.example.healthconnect.navigation.api.NavigationEntry

/** Navigation destinations defined within the permissions module. */
sealed class PermissionNavigationEntry : NavigationEntry {

    /**
     * Permissions overview screen — shows all declared permissions grouped by
     * Read/Write, each with its current grant status and a per-row grant button.
     */
    data object Overview : PermissionNavigationEntry()
}
