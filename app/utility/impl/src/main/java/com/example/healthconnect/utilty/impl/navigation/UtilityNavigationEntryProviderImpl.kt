package com.example.healthconnect.utilty.impl.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.utilty.api.record.Model
import com.example.healthconnect.navigation.api.NavigationEntry
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntry
import com.example.healthconnect.utilty.api.navigation.UtilityNavigationEntryProvider
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.DashboardScreen
import kotlin.reflect.KClass

class UtilityNavigationEntryProviderImpl(
    private val permissionOverview: NavigationEntry,
    private val getRecordsEntry: (recordType: KClass<out Model>, titleRes: Int) -> NavigationEntry,
) : UtilityNavigationEntryProvider {

    override fun getNavEntry(
        key: UtilityNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        showInternalDataManager: () -> Unit,
        innerPadding: PaddingValues?,
    ): NavEntry<NavigationEntry> {
        val defaultPadding = PaddingValues(all = 0.dp)
        return when (key) {
            is UtilityNavigationEntry.Dashboard -> NavEntry(key) {
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
