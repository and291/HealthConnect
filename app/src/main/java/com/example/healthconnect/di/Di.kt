package com.example.healthconnect.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.domain.PayloadMapper
import com.example.healthconnect.domain.ResultMapper
import com.example.healthconnect.domain.usecase.Delete
import com.example.healthconnect.domain.usecase.Insert
import com.example.healthconnect.domain.usecase.Read
import com.example.healthconnect.ui.ParameterlessViewModelFactory
import com.example.healthconnect.ui.RecordsViewModelFactory
import com.example.healthconnect.ui.screen.record.metadata.EditorViewModelFactory
import com.example.healthconnect.ui.screen.record.metadata.DeviceMapper
import com.example.healthconnect.ui.screen.record.metadata.DeviceTypeMapper
import kotlin.reflect.KClass

object Di { //move to dagger. keep all features
    var isPreview = true

    lateinit var applicationContext: Context

    private val libraryRepository by lazy {
        if (isPreview) {
            object : LibraryRepository {
                override fun getSdkStatus(): Int {
                    return HealthConnectClient.SDK_AVAILABLE
                }

                override suspend fun getGrantedPermissions(): Set<String> {
                    return setOf("sdk:permission")
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

    private val insert by lazy {
        Insert(libraryRepository, resultMapper, payloadMapper)
    }

    private val read by lazy {
        Read(libraryRepository, resultMapper, payloadMapper)
    }

    private val delete by lazy {
        Delete(libraryRepository, resultMapper, payloadMapper)
    }

    val parameterlessViewModelFactory by lazy {
        ParameterlessViewModelFactory(applicationContext, libraryRepository, insert)
    }

    val recordsViewModelFactory by lazy {
        RecordsViewModelFactory(read, delete)
    }

    val editorViewModelFactory by lazy {
        EditorViewModelFactory()
    }

    val deviceTypeMapper = DeviceTypeMapper()
    val deviceMapper = DeviceMapper()
}
