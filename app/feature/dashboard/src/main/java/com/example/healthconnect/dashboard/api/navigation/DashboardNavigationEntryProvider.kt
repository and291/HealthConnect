package com.example.healthconnect.dashboard.api.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.navigation.api.NavigationEntry

interface DashboardNavigationEntryProvider {

    fun getNavEntry(
        key: DashboardNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        showInternalDataManager: () -> Unit,
        innerPadding: PaddingValues? = null,
    ): NavEntry<NavigationEntry>
}
