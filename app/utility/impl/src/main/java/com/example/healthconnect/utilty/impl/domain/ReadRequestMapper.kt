package com.example.healthconnect.utilty.impl.domain

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest

class ReadRequestMapper {

    fun <R : Record> map(
        request: ReadRequest<R>,
        pageToken: String? = null,
    ): ReadRecordsRequest<R> = ReadRecordsRequest(
        recordType = request.recordType,
        timeRangeFilter = TimeRangeFilter.before(endTime = request.endTime),
        dataOriginFilter = request.dataOriginFilterPackageName.map { DataOrigin(it) }.toSet(),
        ascendingOrder = request.ascendingOrder,
        pageSize = request.pageSize,
        pageToken = pageToken,
    )
}
