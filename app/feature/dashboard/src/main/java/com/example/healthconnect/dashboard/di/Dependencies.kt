package com.example.healthconnect.dashboard.di

import com.example.healthconnect.dashboard.api.domain.usecase.CountRecords
import com.example.healthconnect.dashboard.api.domain.usecase.GetDashboardCatalog

internal sealed interface Dependencies {

    val getCatalog: GetDashboardCatalog
    val countRecords: CountRecords
}

data class ProductionDependencies(
    override val getCatalog: GetDashboardCatalog,
    override val countRecords: CountRecords,
) : Dependencies
