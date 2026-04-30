package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.ui.mapper.FlowResultTerminalIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapper
import kotlin.reflect.KClass

class DashboardViewModelFactory(
    private val count: Count,
    private val nameMapper: RecordTypeNameMapper,
    private val iconMapper: RecordTypeIconMapper,
    private val flowResultTerminalIconMapper: FlowResultTerminalIconMapper,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return DashboardViewModel(
            count = count,
            nameMapper = nameMapper,
            iconMapper = iconMapper,
            flowResultTerminalIconMapper = flowResultTerminalIconMapper,
        ) as T
    }
}
