package com.example.healthconnect.utilty.impl.ui.mapper

import com.example.healthconnect.utilty.impl.domain.SupportedRecords
import org.junit.Test

class RecordTypeNameMapperTest {

    private val mapper = RecordTypeNameMapper()

    @Test
    fun allLibraryRecordsHaveNameResource() {
        val allTypes = SupportedRecords.instantaneous + SupportedRecords.interval + SupportedRecords.series
        allTypes.forEach { type ->
            mapper.nameRes(type) // throws if missing
        }
    }
}