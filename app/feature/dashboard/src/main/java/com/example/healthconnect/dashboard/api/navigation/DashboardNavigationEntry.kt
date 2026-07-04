package com.example.healthconnect.dashboard.api.navigation

import com.example.healthconnect.navigation.api.NavigationEntry

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.navigation.api.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class DashboardNavigationEntry : NavigationEntry {

    /** Grid of all supported record types with their stored-record counts. App start screen. */
    data object Dashboard : DashboardNavigationEntry()
}
