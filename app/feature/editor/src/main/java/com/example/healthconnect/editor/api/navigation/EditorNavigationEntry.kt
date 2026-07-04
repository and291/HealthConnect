package com.example.healthconnect.editor.api.navigation

import com.example.healthconnect.editor.api.domain.entity.Editable
import com.example.healthconnect.navigation.api.NavigationEntry
import kotlin.reflect.KClass

/**
 * Contains all navigation destinations defined within this module.
 *
 * Naming convention: [%moduleName%]NavigationEntry
 * Each destination must extend [com.example.healthconnect.navigation.api.NavigationEntry] — a base type from this project's navigation framework.
 */
sealed class EditorNavigationEntry : NavigationEntry {

    data class EditRecordScreen(
        val model: Editable,
        val recordClass: KClass<*>,
    ) : EditorNavigationEntry()

    data class Insert(val modelType: KClass<*>) : EditorNavigationEntry()
}