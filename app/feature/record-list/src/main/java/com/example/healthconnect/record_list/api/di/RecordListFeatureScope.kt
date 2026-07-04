package com.example.healthconnect.record_list.api.di

import com.example.healthconnect.record_list.api.domain.usecase.DeleteRecord
import com.example.healthconnect.record_list.api.domain.usecase.LoadRecords
import com.example.healthconnect.record_list.api.ui.RecordSummaryFactory
import com.example.healthconnect.record_list.di.Locator
import com.example.healthconnect.record_list.di.ProductionDependencies
import java.io.Closeable

class RecordListFeatureScope(
    val loadRecords: LoadRecords,
    val deleteRecord: DeleteRecord,
    val summaryFactory: RecordSummaryFactory,
) : Closeable {

    fun init() {
        Locator.impl = ProductionDependencies(
            loadRecords = loadRecords,
            deleteRecord = deleteRecord,
            summaryFactory = summaryFactory,
        )
    }

    override fun close() {
        Locator.clear()
    }
}
