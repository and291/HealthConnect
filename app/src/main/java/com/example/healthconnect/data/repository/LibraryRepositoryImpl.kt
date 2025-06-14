package com.example.healthconnect.data.repository

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.InsertRecordsResponse
import com.example.healthconnect.domain.LibraryRepository

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

    override suspend fun insertRecords(records: List<Record>): InsertRecordsResponse {
        return healthConnectClient.insertRecords(records)
    }
}
