package com.example.healthconnect.dashboard.api.domain.entity

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

/**
 * Static description of a single record type shown on the dashboard: its type token (used as a
 * count/navigation key), display name and icon. Supplied by the app-module integration.
 */
data class DashboardType(
    val type: KClass<*>,
    @param:StringRes val nameRes: Int,
    val icon: ImageVector,
)
