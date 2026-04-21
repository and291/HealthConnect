package com.example.healthconnect.utilty.impl.data.repository

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.data.mapper.FlowResultMapper
import com.example.healthconnect.utilty.impl.data.mapper.ReadParamsMapper
import com.example.healthconnect.utilty.impl.data.mapper.TypeMapper
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
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

    override fun <M : Model> readAll(
        params: ReadParams<M>,
    ): Flow<FlowResult<Page>> = readAllRecords<Record, M>(params)
        .map { response ->
            Log.d("FFFFFF", "handling next page: ${response.records.size}")
            Page(response.records.map { modelFactory.create(it) })
        }
        .map { page -> FlowResult.Data(page) as FlowResult<Page> }
        .catch { e -> emit(flowResultMapper.mapTerminal(e)) }
        .buffer(Channel.RENDEZVOUS)
        .flowOn(Dispatchers.IO)

    override fun <M : Model> count(
        params: ReadParams<M>,
    ): Flow<FlowResult<Int>> = countAllRecords(params)
        .catch { e -> emit(flowResultMapper.mapTerminal(e)) }
        .flowOn(Dispatchers.IO)

    private fun <M : Model> countAllRecords(
        params: ReadParams<M>,
    ): Flow<FlowResult<Int>> = flow {
        val totalCount = readAllRecords<Record, M>(params)
            .fold(0) { accumulated, response -> accumulated + response.records.size }
        emit(FlowResult.Data(totalCount) as FlowResult<Int>)
    }

    private fun <R : Record, M : Model> readAllRecords(
        params: ReadParams<M>,
    ): Flow<ReadRecordsResponse<R>> = flow {
        var continuationToken: String? = null
        do {
            val readRecordsRequest = readParamsMapper.map<R, M>(params, continuationToken)
            val response = healthConnectClient.readRecords(readRecordsRequest)
            emit(response)
            continuationToken = response.pageToken
        } while (continuationToken != null)
    }
}
