package com.example.healthconnect.utilty.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.editor.api.navigation.EditorNavigationEntry
import com.example.healthconnect.navigation.api.NavigationEntry
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntryProvider
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntry
import com.example.healthconnect.utilty.impl.ui.screen.InsertRecordScreen
import com.example.healthconnect.utilty.impl.ui.screen.RecordsScreen

class UtilityNavigationEntryProviderImpl : UtilityNavigationEntryProvider {

    override fun getNavEntry(
        key: UtilityNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        requestPermission: (String) -> Unit,
        innerPadding: PaddingValues?,
    ): NavEntry<NavigationEntry> {
        val defaultPadding = PaddingValues(all = 0.dp)
        return when (key) {
            is UtilityNavigationEntry.Records -> NavEntry(key) {
                RecordsScreen(
                    requestPermission = requestPermission,
                    onRecordClick = {
                        backStack.add(EditorNavigationEntry.EditRecordScreen(it))
                    },
                    onInsertRecordClick = {
                        backStack.add(UtilityNavigationEntry.Insert(key.recordType))
                    },
                    recordType = key.recordType,
                    modifier = Modifier.padding(innerPadding ?: defaultPadding)
                )
            }

            is UtilityNavigationEntry.Insert -> NavEntry(key) {
                InsertRecordScreen(
                    modifier = Modifier.padding(innerPadding ?: defaultPadding),
                )
            }
        }
    }

}