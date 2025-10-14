package com.example.healthconnect.utilty.impl.domain.usecase

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.PayloadMapper
import com.example.healthconnect.utilty.impl.domain.ResultMapper
import com.example.healthconnect.utilty.impl.domain.entity.Result
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
