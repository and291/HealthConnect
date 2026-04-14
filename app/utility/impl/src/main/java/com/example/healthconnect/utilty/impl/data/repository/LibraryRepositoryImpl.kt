package com.example.healthconnect.utilty.impl.data.repository

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.data.mapper.FlowResultMapper
import com.example.healthconnect.utilty.impl.data.mapper.ReadParamsMapper
import com.example.healthconnect.utilty.impl.data.mapper.TypeMapper
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.reflect.KClass

class LibraryRepositoryImpl(
    private val applicationContext: Context,
    private val typeMapper: TypeMapper,
    private val readParamsMapper: ReadParamsMapper,
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

    override suspend fun updateRecords(records: List<Model>) {
        val rec = records.map { modelFactory.createByModel(it) }
        return healthConnectClient.updateRecords(rec)
    }

    override suspend fun insertRecords(records: List<Model>): List<String> {
        val rec = records.map { modelFactory.createByModel(it) }
        return healthConnectClient.insertRecords(rec).recordIdsList
    }

    override suspend fun removeRecord(recordType: KClass<out Model>, metadataId: String) {
        return healthConnectClient.deleteRecords(
            recordType = typeMapper.toRecord(recordType),
            recordIdsList = listOf(metadataId),
            clientRecordIdsList = emptyList(),
        )
    }

    override fun <M : Model> readAll(params: ReadParams<M>): Flow<FlowResult<Model>> = flow {
        try {
            var continuationToken: String? = null
            while (true) {
                with(readPage(params, continuationToken)) {
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

    override fun <M : Model> count(params: ReadParams<M>): Flow<FlowResult<Int>> = flow {
        try {
            var accumulatedCount = 0
            var continuationToken: String? = null
            while (true) {
                val response = readPage<Record, M>(params, continuationToken)
                with(response) {
                    accumulatedCount += records.size
                    emit(FlowResult.Data(accumulatedCount))
                    continuationToken = pageToken ?: break
                }
            }
        } catch (e: Exception) {
            emit(flowResultMapper.mapTerminalState(e))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun <R : Record, M : Model> readPage(
        request: ReadParams<M>,
        continuationToken: String? = null,
    ): ReadRecordsResponse<R> {
        val readRecordsRequest = readParamsMapper.map<R, M>(request, continuationToken)
        return healthConnectClient.readRecords<R>(readRecordsRequest)
    }
}
