package com.example.healthconnect.editor.api.navigation

import androidx.health.connect.client.records.Record
import com.example.healthconnect.navigation.api.NavigationEntry

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.editor.api.navigation.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class EditorNavigationEntry : NavigationEntry {

    data class RecordScreen(val record: Record) : EditorNavigationEntry()
}