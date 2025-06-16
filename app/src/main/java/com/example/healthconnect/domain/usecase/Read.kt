package com.example.healthconnect.domain.usecase

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.domain.PayloadMapper
import com.example.healthconnect.domain.ResultMapper
import com.example.healthconnect.domain.model.Result
import java.time.Instant

class Read(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
    private val payloadMapper: PayloadMapper,
) {

    suspend operator fun invoke(
        recordType: Record
    ): Result {
        val requiredPermission = HealthPermission.getWritePermission(recordType::class)
        if (!libraryRepository.getGrantedPermissions().contains(requiredPermission)) {
            return Result.PermissionRequired(requiredPermission)
        }

        val request = ReadRecordsRequest(
            recordType = recordType::class,
            timeRangeFilter = TimeRangeFilter.after(Instant.now())
        )
        return try {
            val response = libraryRepository.readRecords(request)
            Result.Success(
                payload = payloadMapper.mapReadList(response)
            )
        } catch (e: Exception) {
            resultMapper.mapException(e)
        }
    }
}
