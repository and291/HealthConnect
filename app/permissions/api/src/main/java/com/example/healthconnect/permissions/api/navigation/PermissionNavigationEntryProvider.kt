package com.example.healthconnect.permissions.api.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.navigation.api.NavigationEntry

interface PermissionNavigationEntryProvider {

    /**
     * @param key The destination to resolve.
     * @param backStack Global navigation back stack.
     * @param innerPadding Scaffold padding to apply to the root composable.
     */
    fun getNavEntry(
        key: PermissionNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        innerPadding: PaddingValues? = null,
    ): NavEntry<NavigationEntry>
}
