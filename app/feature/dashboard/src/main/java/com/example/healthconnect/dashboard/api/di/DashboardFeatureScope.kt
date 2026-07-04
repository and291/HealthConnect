package com.example.healthconnect.dashboard.api.di

import com.example.healthconnect.dashboard.api.domain.usecase.CountRecords
import com.example.healthconnect.dashboard.api.domain.usecase.GetDashboardCatalog
import com.example.healthconnect.dashboard.di.Locator
import com.example.healthconnect.dashboard.di.ProductionDependencies
import java.io.Closeable

class DashboardFeatureScope(
    val getCatalog: GetDashboardCatalog,
    val countRecords: CountRecords,
) : Closeable {

    fun init() {
        Locator.impl = ProductionDependencies(
            getCatalog = getCatalog,
            countRecords = countRecords,
        )
    }

    override fun close() {
        Locator.clear()
    }
}
