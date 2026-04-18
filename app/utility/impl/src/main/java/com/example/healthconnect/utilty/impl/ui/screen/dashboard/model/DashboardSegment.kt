package com.example.healthconnect.utilty.impl.ui.screen.dashboard.model

import androidx.annotation.StringRes

data class DashboardSegment(
    @StringRes val title: Int,
    val items: List<DashboardItem>,
)
