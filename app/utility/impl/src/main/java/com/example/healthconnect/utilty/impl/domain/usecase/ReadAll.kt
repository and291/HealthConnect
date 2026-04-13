package com.example.healthconnect.utilty.impl.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import kotlin.reflect.KClass

class ReadAll(
    private val libraryRepository: LibraryRepository,
) {

    operator fun invoke(
        type: KClass<out Model>
    ): Flow<FlowResult<Model>> {
        val request = ReadParams(
            modelType = type,
            endTime = Instant.now(),
        )
        return libraryRepository.readAll(request)
    }
}