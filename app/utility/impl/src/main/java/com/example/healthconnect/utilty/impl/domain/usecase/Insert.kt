package com.example.healthconnect.utilty.impl.domain.usecase

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.PayloadMapper
import com.example.healthconnect.utilty.impl.domain.ResultMapper
import com.example.healthconnect.utilty.impl.domain.entity.Result

class Insert(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
    private val payloadMapper: PayloadMapper,
) {

    suspend operator fun invoke(
        record: Record,
    ): Result {
        val requiredPermission = HealthPermission.getWritePermission(record::class)
        if (!libraryRepository.getGrantedPermissions().contains(requiredPermission)) {
            return Result.PermissionRequired(requiredPermission)
        }

        return try {
            val response = libraryRepository.insertRecords(listOf(record))
            Result.Success(
                payload = payloadMapper.mapInsertList(response)
            )
        } catch (e: Exception) {
            resultMapper.mapException(e)
        }
    }
}
