package com.example.healthconnect.dashboard.api.domain.entity

import androidx.annotation.StringRes

/** A titled group of [DashboardType]s (e.g. instantaneous / interval / series). */
data class DashboardCategory(
    @param:StringRes val titleRes: Int,
    val types: List<DashboardType>,
)
