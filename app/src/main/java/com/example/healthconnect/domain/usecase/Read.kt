package com.example.healthconnect.domain.usecase

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.healthconnect.domain.LibraryRepository
import com.example.healthconnect.domain.PayloadMapper
import com.example.healthconnect.domain.ResultMapper
import com.example.healthconnect.domain.entity.Result
import java.time.Instant
import kotlin.reflect.KClass

class Read(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
    private val payloadMapper: PayloadMapper,
) {

    suspend operator fun invoke(
        recordType: KClass<out Record>
    ): Result {
        val requiredPermission = HealthPermission.getReadPermission(recordType)
        if (!libraryRepository.getGrantedPermissions().contains(requiredPermission)) {
            return Result.PermissionRequired(requiredPermission)
        }

        val request = ReadRecordsRequest(
            recordType = recordType,
            timeRangeFilter = TimeRangeFilter.before(Instant.now())
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
