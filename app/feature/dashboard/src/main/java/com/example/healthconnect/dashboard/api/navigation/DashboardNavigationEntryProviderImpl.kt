package com.example.healthconnect.dashboard.api.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.dashboard.ui.screen.dashboard.DashboardScreen
import com.example.healthconnect.navigation.api.NavigationEntry
import kotlin.reflect.KClass

class DashboardNavigationEntryProviderImpl(
    private val permissionOverview: NavigationEntry,
    private val getRecordsEntry: (recordType: KClass<*>, titleRes: Int) -> NavigationEntry,
) : DashboardNavigationEntryProvider {

    override fun getNavEntry(
        key: DashboardNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        showInternalDataManager: () -> Unit,
        innerPadding: PaddingValues?,
    ): NavEntry<NavigationEntry> {
        val defaultPadding = PaddingValues(all = 0.dp)
        return when (key) {
            is DashboardNavigationEntry.Dashboard -> NavEntry(key) {
                DashboardScreen(
                    onTypeClick = { type, nameRes ->
                        backStack.add(getRecordsEntry(type, nameRes))
                    },
                    onShowLibraryDataManager = showInternalDataManager,
                    onNavigateToPermissions = {
                        backStack.add(permissionOverview)
                    },
                    modifier = Modifier.padding(innerPadding ?: defaultPadding),
                )
            }
        }
    }
}
