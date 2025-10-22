package com.example.healthconnect.utilty.api.navigation

import androidx.health.connect.client.records.Record
import com.example.healthconnect.navigation.api.NavigationEntry
import kotlin.reflect.KClass

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.navigation.api.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class UtilityNavigationEntry : NavigationEntry {

    data class Records(val recordType: KClass<Record>) : UtilityNavigationEntry()
    data class Insert(val recordType: KClass<Record>) : UtilityNavigationEntry()
}