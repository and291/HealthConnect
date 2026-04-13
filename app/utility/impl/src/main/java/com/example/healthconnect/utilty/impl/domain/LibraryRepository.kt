package com.example.healthconnect.utilty.impl.domain

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass


interface LibraryRepository {

    fun getSdkStatus(): Int
    suspend fun getGrantedPermissions(): Set<String>
    suspend fun updateRecords(records: List<Model>)
    suspend fun insertRecords(records: List<Model>): List<String>
    suspend fun removeRecord(recordType: KClass<out Model>, metadataId: String)
    fun <M : Model> readPages(params: ReadParams<M>): Channel<FlowResult<Page<Model>>>
    fun <M : Model> readAll(params: ReadParams<M>): Flow<FlowResult<Model>>
    fun <M : Model> count(params: ReadParams<M>): Flow<FlowResult<Int>>
}
