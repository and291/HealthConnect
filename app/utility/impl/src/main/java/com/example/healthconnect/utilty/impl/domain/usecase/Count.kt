package com.example.healthconnect.utilty.impl.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import kotlin.reflect.KClass

class Count(
    private val libraryRepository: LibraryRepository,
) {

    operator fun invoke(
        type: KClass<out Model>
    ): Flow<FlowResult<Int>> {
        val request = ReadRequest(
            modelType = type,
            endTime = Instant.now(),
            pageSize = 5_000,
        )
        return libraryRepository.count(request)
    }
}