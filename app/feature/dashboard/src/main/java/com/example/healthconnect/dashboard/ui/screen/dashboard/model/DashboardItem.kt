package com.example.healthconnect.dashboard.ui.screen.dashboard.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

data class DashboardItem(
    val recordType: KClass<*>,
    @param:StringRes val nameRes: Int,
    val icon: ImageVector,
    val state: LoadingState,
) {
    sealed class LoadingState {

        data object InProgress : LoadingState()

        data class Counted(
            val count: Int,
        ) : LoadingState()

        data class Failed(
            val errorIcon: ImageVector,
        ) : LoadingState()
    }
}
