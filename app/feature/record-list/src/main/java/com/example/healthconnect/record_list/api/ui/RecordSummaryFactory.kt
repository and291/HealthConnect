package com.example.healthconnect.record_list.api.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthconnect.record_list.api.domain.entity.RecordModel

/**
 * Renders a compact, read-only summary of a [RecordModel]. Supplied by the app-module integration
 * so this feature stays independent of the record/field rendering types.
 */
interface RecordSummaryFactory {

    @Composable
    fun Summary(record: RecordModel, modifier: Modifier = Modifier)
}
