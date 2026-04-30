package com.example.healthconnect.permissions.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.navigation.api.NavigationEntry
import com.example.healthconnect.permissions.api.navigation.PermissionNavigationEntry
import com.example.healthconnect.permissions.api.navigation.PermissionNavigationEntryProvider
import com.example.healthconnect.permissions.impl.ui.screen.PermissionsScreen

class PermissionNavigationEntryProviderImpl : PermissionNavigationEntryProvider {

    override fun getNavEntry(
        key: PermissionNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        innerPadding: PaddingValues?,
    ): NavEntry<NavigationEntry> = when (key) {
        PermissionNavigationEntry.Overview -> NavEntry(key) {
            PermissionsScreen(
                modifier = Modifier.padding(innerPadding ?: PaddingValues(all = 0.dp)),
            )
        }
    }
}
