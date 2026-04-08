package com.example.healthconnect.utilty.impl.domain.usecase

import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import kotlin.reflect.KClass

class Count(
    private val libraryRepository: LibraryRepository,
) {

    operator fun invoke(
        type: KClass<out Record>
    ): Flow<FlowResult<Int>> {
        val request = ReadRequest(
            recordType = type,
            endTime = Instant.now(),
            pageSize = 5_000,
        )
        return libraryRepository.count(request)
    }
}