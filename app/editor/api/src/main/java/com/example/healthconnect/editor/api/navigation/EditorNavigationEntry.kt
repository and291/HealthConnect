package com.example.healthconnect.editor.api.navigation

import androidx.health.connect.client.records.Record
import com.example.healthconnect.navigation.api.NavigationEntry
import kotlin.reflect.KClass

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.navigation.api.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class EditorNavigationEntry : NavigationEntry {

    data class EditRecordScreen(val record: Record) : EditorNavigationEntry()

    data class Insert(val recordType: KClass<Record>) : EditorNavigationEntry()
}