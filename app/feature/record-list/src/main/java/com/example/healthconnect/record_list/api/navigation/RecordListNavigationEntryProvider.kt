package com.example.healthconnect.record_list.api.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.navigation.api.NavigationEntry

interface RecordListNavigationEntryProvider {

    fun getNavEntry(
        key: RecordListNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        innerPadding: PaddingValues? = null,
    ): NavEntry<NavigationEntry>
}
