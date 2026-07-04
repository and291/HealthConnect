package com.example.healthconnect.dashboard.ui.screen.dashboard.model

import androidx.annotation.StringRes

data class DashboardSegment(
    @param:StringRes val title: Int,
    val items: List<DashboardItem>,
)
