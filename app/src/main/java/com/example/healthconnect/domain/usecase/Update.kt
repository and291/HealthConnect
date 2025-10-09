package com.example.healthconnect.domain.usecase

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.domain.PayloadMapper
import com.example.healthconnect.domain.ResultMapper
import com.example.healthconnect.domain.model.Result

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