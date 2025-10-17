package com.example.healthconnect.utilty.impl.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.components.api.ui.ComponentProvider
import com.example.healthconnect.utilty.impl.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.PayloadMapper
import com.example.healthconnect.utilty.impl.domain.ResultMapper
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.Insert
import com.example.healthconnect.utilty.impl.domain.usecase.Read
import com.example.healthconnect.utilty.impl.domain.usecase.Update
import com.example.healthconnect.utilty.impl.ui.mapper.DeviceMapper
import com.example.healthconnect.utilty.impl.ui.mapper.MetadataMapper
import com.example.healthconnect.utilty.impl.ui.RecordsViewModelFactory
import com.example.healthconnect.utilty.impl.ui.screen.record.RecordViewModelFactory
import com.example.healthconnect.utilty.impl.ui.screen.record.mapper.RecordMapper
import kotlin.reflect.KClass

object Di { //TODO move to dagger. keep all features
    var isPreview = true

    lateinit var applicationContext: Context
    lateinit var componentProvider: ComponentProvider

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

    private val insert by lazy {
        Insert(libraryRepository, resultMapper, payloadMapper)
    }

    private val update by lazy {
        Update(libraryRepository, resultMapper, payloadMapper)
    }

    private val read by lazy {
        Read(libraryRepository, resultMapper, payloadMapper)
    }

    private val delete by lazy {
        Delete(libraryRepository, resultMapper, payloadMapper)
    }

    val recordsViewModelFactory by lazy {
        RecordsViewModelFactory(read, delete)
    }

    val recordViewModelFactory by lazy {
        RecordViewModelFactory(
            recordMapper = recordMapper,
            metadataMapper = metadataMapper,
            update = update,
        )
    }

    private val deviceMapper = DeviceMapper()
    private val metadataMapper = MetadataMapper(
        deviceMapper = deviceMapper,
    )
    private val recordMapper = RecordMapper(
        metadataMapper = metadataMapper,
    )
}
