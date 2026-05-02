package com.example.healthconnect.utilty.impl.ui.mapper

import com.example.healthconnect.utilty.impl.domain.SupportedModels
import org.junit.Test

class RecordTypeNameMapperTest {

    private val mapper = RecordTypeNameMapperImpl()

    @Test
    fun allLibraryRecordsHaveNameResource() {
        val allTypes = SupportedModels.instantaneous + SupportedModels.interval + SupportedModels.series
        allTypes.forEach { type ->
            mapper.nameRes(type) // throws if missing
        }
    }
}