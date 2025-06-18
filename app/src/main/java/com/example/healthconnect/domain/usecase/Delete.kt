package com.example.healthconnect.domain.usecase

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.domain.PayloadMapper
import com.example.healthconnect.domain.ResultMapper
import com.example.healthconnect.domain.model.Result
import kotlin.reflect.KClass

class Delete(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
    private val payloadMapper: PayloadMapper,
) {

    suspend operator fun invoke(
        recordType: KClass<out Record>,
        metadataId: String,
    ): Result {
        val requiredPermission = HealthPermission.getWritePermission(recordType)
        if (!libraryRepository.getGrantedPermissions().contains(requiredPermission)) {
            return Result.PermissionRequired(requiredPermission)
        }

        return try {
            libraryRepository.removeRecord(
                recordType = recordType,
                metadataId = metadataId,
            )
            Result.Success(
                payload = payloadMapper.mapDeletedRecord()
            )
        } catch (e: Exception) {
            resultMapper.mapException(e)
        }
    }
}
