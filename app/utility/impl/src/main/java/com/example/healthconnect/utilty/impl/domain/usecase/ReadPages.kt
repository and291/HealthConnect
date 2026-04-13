package com.example.healthconnect.utilty.impl.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import kotlinx.coroutines.channels.Channel
import java.time.Instant
import kotlin.reflect.KClass

class ReadPages(
    private val libraryRepository: LibraryRepository,
) {

    operator fun invoke(
        type: KClass<out Model>
    ): Channel<FlowResult<Page<Model>>> {
        val request = ReadParams(
            modelType = type,
            endTime = Instant.now(),
            pageSize = 30,
            ascendingOrder = false,
        )
        return libraryRepository.readPages(request)
    }
}