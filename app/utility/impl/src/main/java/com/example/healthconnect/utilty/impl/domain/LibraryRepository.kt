package com.example.healthconnect.utilty.impl.domain

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import kotlin.reflect.KClass


//Domain layer is closely bound to Health Connect SDK API
interface LibraryRepository {

    fun getSdkStatus(): Int
    suspend fun getGrantedPermissions(): Set<String>
    suspend fun updateRecords(records: List<Record>)
    suspend fun insertRecords(records: List<Record>): InsertRecordsResponse
    suspend fun <T : Record> readRecords(request: ReadRecordsRequest<T>): ReadRecordsResponse<T>
    suspend fun removeRecord(recordType: KClass<out Record>, metadataId: String)
}
