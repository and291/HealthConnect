package com.example.healthconnect.utilty.impl.ui.screen.dashboard.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.health.connect.client.records.Record
import kotlin.reflect.KClass

data class DashboardItem(
    val recordType: KClass<out Record>,
    @param:StringRes val nameRes: Int,
    val icon: ImageVector,
    val count: Int?,
)
