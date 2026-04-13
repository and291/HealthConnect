package com.example.healthconnect.utilty.impl.data.mapper

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import kotlin.reflect.KClass

class ReadParamsMapper(
    private val typeMapper: TypeMapper
) {

    @Suppress("UNCHECKED_CAST")
    fun <R : Record, M : Model> map(
        params: ReadParams<M>,
        pageToken: String? = null,
    ): ReadRecordsRequest<R> = ReadRecordsRequest<R>(
        recordType = typeMapper.toRecord(params.modelType) as KClass<R>,
        timeRangeFilter = TimeRangeFilter.before(endTime = params.endTime),
        dataOriginFilter = params.dataOriginFilterPackageName.map { DataOrigin(it) }.toSet(),
        ascendingOrder = params.ascendingOrder,
        pageSize = params.pageSize,
        pageToken = pageToken,
    )
}
