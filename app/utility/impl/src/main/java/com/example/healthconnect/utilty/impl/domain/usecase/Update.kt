package com.example.healthconnect.utilty.impl.domain.usecase

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.PayloadMapper
import com.example.healthconnect.utilty.impl.domain.ResultMapper
import com.example.healthconnect.utilty.impl.domain.entity.Result

class Update(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
    private val payloadMapper: PayloadMapper,
) {

    suspend operator fun invoke(
        modifiedRecord: Record,
    ): Result {
        val requiredPermission = HealthPermission.getWritePermission(modifiedRecord::class)
        if (!libraryRepository.getGrantedPermissions().contains(requiredPermission)) {
            return Result.PermissionRequired(requiredPermission)
        }

        return try {
            libraryRepository.updateRecords(listOf(modifiedRecord))
            Result.Success(
                payload = payloadMapper.mapUpdateRecord()
            )
        } catch (e: Exception) {
            resultMapper.mapException(e)
        }
    }
}