package com.example.healthconnect.utilty.impl.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.api.domain.usecase.Update
import com.example.healthconnect.utilty.impl.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.PayloadMapper
import com.example.healthconnect.utilty.impl.domain.ResultMapper
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.InsertImpl
import com.example.healthconnect.utilty.impl.domain.usecase.Read
import com.example.healthconnect.utilty.impl.domain.usecase.UpdateImpl
import com.example.healthconnect.utilty.impl.ui.RecordsViewModelFactory
import kotlin.reflect.KClass

object Di { //TODO move to dagger. keep all features
    var isPreview = true

    lateinit var applicationContext: Context
    lateinit var modelFactory: ModelFactory

    private val libraryRepository by lazy {
        if (isPreview) {
            object : LibraryRepository {
                override fun getSdkStatus(): Int {
                    return HealthConnectClient.SDK_AVAILABLE
                }

                override suspend fun getGrantedPermissions(): Set<String> {
                    return setOf("sdk:permission")
                }

                override suspend fun updateRecords(records: List<Record>) {
                    TODO("Not yet implemented")
                }

                override suspend fun insertRecords(records: List<Record>): InsertRecordsResponse {
                    TODO("Not yet implemented")
                }

                override suspend fun <T : Record> readRecords(request: ReadRecordsRequest<T>): ReadRecordsResponse<T> {
                    TODO("Not yet implemented")
                }

                override suspend fun removeRecord(
                    recordType: KClass<out Record>,
                    metadataId: String
                ) {
                    TODO("Not yet implemented")
                }
            }
        } else {
            LibraryRepositoryImpl(applicationContext)
        }
    }

    private val payloadMapper = PayloadMapper()
    private val resultMapper = ResultMapper()

    val insert: Insert by lazy {
        InsertImpl(libraryRepository, resultMapper, payloadMapper)
    }

    val update: Update by lazy {
        UpdateImpl(libraryRepository, resultMapper, payloadMapper)
    }

    private val read by lazy {
        Read(libraryRepository, resultMapper, payloadMapper)
    }

    private val delete by lazy {
        Delete(libraryRepository, resultMapper, payloadMapper)
    }

    val recordsViewModelFactory by lazy {
        RecordsViewModelFactory(read, delete, modelFactory)
    }
}
