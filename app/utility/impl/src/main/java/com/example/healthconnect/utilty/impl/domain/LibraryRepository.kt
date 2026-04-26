package com.example.healthconnect.utilty.impl.domain

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass


interface LibraryRepository {

    fun getSdkStatus(): Int
    suspend fun getGrantedPermissions(): Set<String>
    suspend fun updateRecords(records: List<Model>)
    suspend fun insertRecords(records: List<Model>): List<String>
    suspend fun removeRecord(recordType: KClass<out Model>, metadataId: String)
    fun <M : Model> pager(params: ReadParams<M>): Pager
    fun <M : Model> count(params: ReadParams<M>): Flow<FlowResult<Int>>
}
