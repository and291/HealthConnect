package com.example.healthconnect.utilty.impl.ui.mapper

import com.example.healthconnect.utilty.impl.domain.SupportedModels
import org.junit.Test

class RecordTypeIconMapperTest {

    private val mapper = RecordTypeIconMapper()

    @Test
    fun allLibraryRecordsHaveIcon() {
        val allTypes = SupportedModels.instantaneous + SupportedModels.interval + SupportedModels.series
        allTypes.forEach { type ->
            mapper.icon(type) // throws if missing
        }
    }
}