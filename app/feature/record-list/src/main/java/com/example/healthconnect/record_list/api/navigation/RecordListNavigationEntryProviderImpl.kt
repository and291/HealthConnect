package com.example.healthconnect.record_list.api.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import com.example.healthconnect.navigation.api.NavigationEntry
import com.example.healthconnect.record_list.api.domain.entity.RecordModel
import com.example.healthconnect.record_list.ui.screen.records.RecordsScreen
import kotlin.reflect.KClass

class RecordListNavigationEntryProviderImpl(
    private val getEditEntry: (record: RecordModel) -> NavigationEntry,
    private val getInsertEntry: (recordType: KClass<*>) -> NavigationEntry,
) : RecordListNavigationEntryProvider {

    override fun getNavEntry(
        key: RecordListNavigationEntry,
        backStack: SnapshotStateList<NavigationEntry>,
        innerPadding: PaddingValues?,
    ): NavEntry<NavigationEntry> {
        val defaultPadding = PaddingValues(all = 0.dp)
        return when (key) {
            is RecordListNavigationEntry.List -> NavEntry(key) {
                RecordsScreen(
                    onRecordClick = { backStack.add(getEditEntry(it)) },
                    onInsertRecordClick = { backStack.add(getInsertEntry(key.recordType)) },
                    recordType = key.recordType,
                    title = stringResource(key.titleRes),
                    modifier = Modifier.padding(innerPadding ?: defaultPadding),
                )
            }
        }
    }
}
