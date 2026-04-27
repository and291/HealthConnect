package com.example.healthconnect.utilty.impl.ui.screen.dashboard.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlin.reflect.KClass

data class DashboardItem(
    val recordType: KClass<out Model>,
    @param:StringRes val nameRes: Int,
    val icon: ImageVector,
    val state: LoadingState,
) {
    sealed class LoadingState {

        data object InProgress : LoadingState()

        data class Loaded(
            val result: FlowResult<Int>,
        ) : LoadingState()
    }
}
