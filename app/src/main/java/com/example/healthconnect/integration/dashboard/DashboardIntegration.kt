package com.example.healthconnect.integration.dashboard

import com.example.healthconnect.dashboard.api.domain.entity.CountResult
import com.example.healthconnect.dashboard.api.domain.entity.DashboardCategory
import com.example.healthconnect.dashboard.api.domain.entity.DashboardType
import com.example.healthconnect.dashboard.api.domain.usecase.CountRecords
import com.example.healthconnect.dashboard.api.domain.usecase.GetDashboardCatalog
import com.example.healthconnect.utilty.api.domain.record.Model
import com.example.healthconnect.utilty.api.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.R
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.ui.mapper.FlowResultTerminalIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass
import com.example.healthconnect.utilty.impl.di.Di as UtilityDi

/**
 * Assembles the dashboard catalog from the supported record types (see utility's `SupportedModels`)
 * and the name/icon mappers. Segment titles come from utility's string resources.
 */
internal class GetDashboardCatalogImpl(
    private val nameMapper: RecordTypeNameMapper,
    private val iconMapper: RecordTypeIconMapper,
) : GetDashboardCatalog {

    override fun invoke(): List<DashboardCategory> = listOf(
        category(R.string.dashboard_segment_instantaneous, UtilityDi.supportedInstantaneous),
        category(R.string.dashboard_segment_interval, UtilityDi.supportedInterval),
        category(R.string.dashboard_segment_series, UtilityDi.supportedSeries),
    )

    private fun category(
        titleRes: Int,
        types: List<KClass<out Model>>,
    ): DashboardCategory = DashboardCategory(
        titleRes = titleRes,
        types = types.map { type ->
            DashboardType(
                type = type,
                nameRes = nameMapper.nameRes(type),
                icon = iconMapper.icon(type),
            )
        },
    )
}

internal class CountRecordsImpl(
    private val count: Count,
    private val iconMapper: FlowResultTerminalIconMapper,
) : CountRecords {

    override fun invoke(type: KClass<*>): Flow<CountResult> {
        @Suppress("UNCHECKED_CAST")
        return count(type as KClass<out Model>).map { it.toCountResult() }
    }

    private fun FlowResult<Int>.toCountResult(): CountResult = when (this) {
        is FlowResult.Data<Int> -> CountResult.Counted(item)
        is FlowResult.Terminal -> CountResult.Failed(iconMapper.icon(this))
    }
}
