package com.example.healthconnect.utilty.impl.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.api.domain.entity.Payload
import com.example.healthconnect.utilty.api.domain.entity.Result
import com.example.healthconnect.utilty.api.domain.usecase.Insert
import com.example.healthconnect.utilty.impl.data.mapper.ResultMapper
import com.example.healthconnect.utilty.impl.domain.LibraryRepository

class InsertImpl(
    private val libraryRepository: LibraryRepository,
    private val resultMapper: ResultMapper,
): Insert {

    override suspend operator fun invoke(
        record: Model,
    ): Result = try {
        val response = libraryRepository.insertRecords(listOf(record))
        Result.Success(
            payload = Payload.InsertList(response)
        )
    } catch (e: Exception) {
        resultMapper.mapException(e)
    }
}
