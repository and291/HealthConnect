package com.example.healthconnect.utilty.impl.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.api.domain.usecase.Update
import com.example.healthconnect.utilty.impl.data.repository.LibraryRepositoryImpl
import com.example.healthconnect.utilty.impl.domain.FlowResultMapper
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.PayloadMapper
import com.example.healthconnect.utilty.impl.domain.ReadRequestMapper
import com.example.healthconnect.utilty.impl.domain.ResultMapper
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.domain.usecase.InsertImpl
import com.example.healthconnect.utilty.impl.domain.usecase.Read
import com.example.healthconnect.utilty.impl.domain.usecase.UpdateImpl
import com.example.healthconnect.utilty.impl.ui.RecordsViewModelFactory
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.DashboardViewModelFactory
import kotlinx.coroutines.flow.Flow
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

                override suspend fun updateRecords(records: List<Record>) = error("No impl")

                override suspend fun insertRecords(records: List<Record>): InsertRecordsResponse = error("No impl")

                override suspend fun <T : Record> readRecords(request: ReadRecordsRequest<T>): ReadRecordsResponse<T> = error("No impl")

                override suspend fun removeRecord(recordType: KClass<out Record>, metadataId: String) = error("No impl")

                override fun <R : Record> readAll(request: ReadRequest<R>): Flow<FlowResult<Model>> = error("No impl")

                override fun <R : Record> count(request: ReadRequest<R>): Flow<FlowResult<Int>> = error("No impl")
            }
        } else {
            LibraryRepositoryImpl(
                applicationContext = applicationContext,
                readRequestMapper = readRequestMapper,
                modelFactory = modelFactory,
                flowResultMapper = flowResultMapper,
            )
        }
    }

    private val payloadMapper = PayloadMapper()
    private val resultMapper = ResultMapper()
    private val readRequestMapper = ReadRequestMapper()
    private val flowResultMapper = FlowResultMapper()

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

    private val recordTypeNameMapper by lazy { RecordTypeNameMapper() }
    private val recordTypeIconMapper by lazy { RecordTypeIconMapper() }

    private val count by lazy {
        Count(libraryRepository)
    }

    internal val dashboardViewModelFactory by lazy {
        DashboardViewModelFactory(count, recordTypeNameMapper, recordTypeIconMapper)
    }
}
