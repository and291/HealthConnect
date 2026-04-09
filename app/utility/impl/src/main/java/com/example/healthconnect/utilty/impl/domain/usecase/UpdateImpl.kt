package com.example.healthconnect.utilty.impl.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.data.mapper.PayloadMapper
import com.example.healthconnect.utilty.impl.data.mapper.ResultMapper
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.api.domain.usecase.Update

class UpdateImpl(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
    private val payloadMapper: PayloadMapper,
) : Update {

    override suspend operator fun invoke(
        modifiedRecord: Model,
    ): Result = try {
        libraryRepository.updateRecords(listOf(modifiedRecord))
        Result.Success(
            payload = payloadMapper.mapUpdateRecord()
        )
    } catch (e: Exception) {
        resultMapper.mapException(e)
    }
}