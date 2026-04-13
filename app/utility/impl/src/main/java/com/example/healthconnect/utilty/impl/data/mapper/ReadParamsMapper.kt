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
        request: ReadParams<M>,
        pageToken: String? = null,
    ): ReadRecordsRequest<R> = ReadRecordsRequest<R>(
        recordType = typeMapper.toRecord(request.modelType) as KClass<R>,
        timeRangeFilter = TimeRangeFilter.before(endTime = request.endTime),
        dataOriginFilter = request.dataOriginFilterPackageName.map { DataOrigin(it) }.toSet(),
        ascendingOrder = request.ascendingOrder,
        pageSize = request.pageSize,
        pageToken = pageToken,
    )
}