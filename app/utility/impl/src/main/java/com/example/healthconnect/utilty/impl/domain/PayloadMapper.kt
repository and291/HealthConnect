package com.example.healthconnect.utilty.impl.domain

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.utilty.api.entity.Payload

class PayloadMapper {

    fun mapInsertList(
        response: InsertRecordsResponse
    ): Payload = Payload.InsertList(
        recordIdsList = response.recordIdsList
    )

    fun <T : Record> mapReadList(
        response: ReadRecordsResponse<T>
    ): Payload = Payload.ReadList<T>(
        list = response.records,
        pageToken = response.pageToken
    )

    fun mapDeletedRecord(): Payload = Payload.Removed

    fun mapUpdateRecord(): Payload = Payload.Updated
}
