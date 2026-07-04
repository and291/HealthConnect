package com.example.healthconnect.utilty.api.navigation

import com.example.healthconnect.navigation.api.NavigationEntry

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.navigation.api.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class UtilityNavigationEntry : NavigationEntry {

    data object Dashboard : UtilityNavigationEntry()
}