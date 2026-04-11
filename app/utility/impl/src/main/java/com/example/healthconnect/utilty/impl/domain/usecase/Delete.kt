package com.example.healthconnect.utilty.impl.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.impl.data.mapper.PayloadMapper
import com.example.healthconnect.utilty.impl.data.mapper.ResultMapper
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import kotlin.reflect.KClass

class Delete(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
    private val payloadMapper: PayloadMapper,
) {

    suspend operator fun invoke(
        recordType: KClass<out Model>,
        metadataId: String,
    ): Result = try {
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
