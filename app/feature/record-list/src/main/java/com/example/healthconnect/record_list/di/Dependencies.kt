package com.example.healthconnect.record_list.di

import com.example.healthconnect.record_list.api.domain.usecase.DeleteRecord
import com.example.healthconnect.record_list.api.domain.usecase.LoadRecords
import com.example.healthconnect.record_list.api.ui.RecordSummaryFactory

internal sealed interface Dependencies {

    val loadRecords: LoadRecords
    val deleteRecord: DeleteRecord
    val summaryFactory: RecordSummaryFactory
}

data class ProductionDependencies(
    override val loadRecords: LoadRecords,
    override val deleteRecord: DeleteRecord,
    override val summaryFactory: RecordSummaryFactory,
) : Dependencies
