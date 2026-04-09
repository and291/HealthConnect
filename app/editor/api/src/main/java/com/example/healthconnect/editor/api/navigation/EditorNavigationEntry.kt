package com.example.healthconnect.editor.api.navigation

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.navigation.api.NavigationEntry
import kotlin.reflect.KClass

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.navigation.api.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class EditorNavigationEntry : NavigationEntry {

    data class EditRecordScreen(val model: Model) : EditorNavigationEntry()

    data class Insert(val modelType: KClass<out Model>) : EditorNavigationEntry()
}