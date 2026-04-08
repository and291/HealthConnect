package com.example.healthconnect.utilty.impl.data.repository

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.utilty.impl.domain.FlowResultMapper
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.ReadRequestMapper
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.reflect.KClass

class LibraryRepositoryImpl(
    private val applicationContext: Context,
    private val readRequestMapper: ReadRequestMapper,
    private val flowResultMapper: FlowResultMapper,
    private val modelFactory: ModelFactory,
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

    override fun <R : Record> readAll(request: ReadRequest<R>): Flow<FlowResult<Model>> = flow {
        try {
            var continuationToken: String? = null
            while (true) {
                with(readPage(request, continuationToken)) {
                    records.forEach {
                        val model = modelFactory.create(it)
                        emit(FlowResult.Data(model))
                    }
                    continuationToken = pageToken ?: break
                }
            }
        } catch (e: Exception) {
            emit(flowResultMapper.mapTerminalState(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun <R : Record> count(request: ReadRequest<R>): Flow<FlowResult<Int>> = flow {
        try {
            var continuationToken: String? = null
            while (true) {
                val response = readPage(request, continuationToken)
                with(response) {
                    emit(FlowResult.Data(records.size))
                    continuationToken = pageToken ?: break
                }
            }
        } catch (e: Exception) {
            emit(flowResultMapper.mapTerminalState(e))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun <R : Record> readPage(
        request: ReadRequest<R>,
        continuationToken: String? = null,
    ): ReadRecordsResponse<R> {
        val readRecordsRequest = readRequestMapper.map(request, continuationToken)
        return healthConnectClient.readRecords<R>(readRecordsRequest)
    }
}
