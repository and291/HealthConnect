package com.example.healthconnect.record_list.api.navigation

import androidx.annotation.StringRes
import com.example.healthconnect.navigation.api.NavigationEntry
import kotlin.reflect.KClass

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.navigation.api.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class RecordListNavigationEntry : NavigationEntry {

    /**
     * List of stored records for a single [recordType]. [titleRes] is the human-readable name of
     * the type, shown as the screen header.
     */
    data class List(
        val recordType: KClass<*>,
        @param:StringRes val titleRes: Int,
    ) : RecordListNavigationEntry()
}
