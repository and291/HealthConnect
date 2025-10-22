package com.example.healthconnect.editor.api.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.navigation.api.NavigationEntry

interface EditorNavigationEntryProvider {

    fun getNavEntry(
        key: EditorNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        innerPadding: PaddingValues? = null
    ): NavEntry<NavigationEntry>
}