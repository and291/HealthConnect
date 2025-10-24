package com.example.healthconnect.editor.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntry
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntryProvider
import com.example.healthconnect.editor.impl.ui.screen.record.EditRecordScreen
import com.example.healthconnect.editor.impl.ui.screen.record.InsertRecordScreen
import com.example.healthconnect.navigation.api.NavigationEntry

class EditorNavigationEntryProviderImpl : EditorNavigationEntryProvider {

    override fun getNavEntry(
        key: EditorNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        innerPadding: PaddingValues?,
    ): NavEntry<NavigationEntry> {
        val defaultPadding = PaddingValues(all = 0.dp)
        return when (key) {
            is EditorNavigationEntry.EditRecordScreen -> NavEntry(key) {
                EditRecordScreen(
                    initialRecord = key.record,
                    modifier = Modifier.padding(innerPadding ?: defaultPadding),
                )
            }

            is EditorNavigationEntry.Insert -> NavEntry(key) {
                InsertRecordScreen(
                    recordClass = key.recordType
                )
            }
        }
    }
}