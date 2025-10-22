package com.example.healthconnect.utilty.api.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.navigation.api.NavigationEntry

interface UtilityNavigationEntryProvider {

    fun getNavEntry(
        key: UtilityNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        requestPermission: (String) -> Unit,
        innerPadding: PaddingValues? = null
    ): NavEntry<NavigationEntry>
}