package com.example.healthconnect.utilty.impl.domain.usecase

import androidx.health.connect.client.records.Record
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import java.util.UUID
import kotlin.reflect.KClass

class GetEditable(
    private val libraryRepository: LibraryRepository,
) {
    suspend operator fun invoke(uuid: UUID, type: KClass<out Record>): Record {
        //TODO handle errors
        return libraryRepository.fetchRecordById(recordId = uuid.toString(), kClass = type).record
    }
}