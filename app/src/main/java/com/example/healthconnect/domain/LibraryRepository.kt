package com.example.healthconnect.domain

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.InsertRecordsResponse


//Domain layer is closely bound to Health Connect SDK API
interface LibraryRepository {

    fun getSdkStatus(): Int
    suspend fun getGrantedPermissions(): Set<String>
    suspend fun insertRecords(records: List<Record>): InsertRecordsResponse
}
