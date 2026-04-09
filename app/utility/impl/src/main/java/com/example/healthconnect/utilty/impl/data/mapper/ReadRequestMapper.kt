package com.example.healthconnect.utilty.impl.data.mapper

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import kotlin.reflect.KClass

class ReadRequestMapper(
    private val typeMapper: TypeMapper
) {

    fun <R : Record, M : Model> map(
        request: ReadRequest<M>,
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