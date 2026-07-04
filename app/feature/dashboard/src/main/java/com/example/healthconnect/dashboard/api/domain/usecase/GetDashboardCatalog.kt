package com.example.healthconnect.dashboard.api.domain.usecase

import com.example.healthconnect.dashboard.api.domain.entity.DashboardCategory

interface GetDashboardCatalog {

    operator fun invoke(): List<DashboardCategory>
}
