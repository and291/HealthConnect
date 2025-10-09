package com.example.healthconnect.data.repository

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.domain.LibraryRepository
import kotlin.reflect.KClass

class LibraryRepositoryImpl(
    private val applicationContext: Context
) : LibraryRepository {

    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(applicationContext) }

    override fun getSdkStatus(): Int {
        return HealthConnectClient.getSdkStatus(applicationContext)
    }

    override suspend fun getGrantedPermissions(): Set<String> {
        return healthConnectClient.permissionController.getGrantedPermissions()
    }

    override suspend fun updateRecords(records: List<Record>) {
        return healthConnectClient.updateRecords(records)
    }

    override suspend fun insertRecords(records: List<Record>): InsertRecordsResponse {
        return healthConnectClient.insertRecords(records)
    }

    override suspend fun <T : Record> readRecords(request: ReadRecordsRequest<T>): ReadRecordsResponse<T> {
        return healthConnectClient.readRecords(request)
    }

    override suspend fun removeRecord(recordType: KClass<out Record>, metadataId: String) {
        return healthConnectClient.deleteRecords(
            recordType = recordType,
            recordIdsList = listOf(metadataId),
            clientRecordIdsList = emptyList(),
        )
    }
}
