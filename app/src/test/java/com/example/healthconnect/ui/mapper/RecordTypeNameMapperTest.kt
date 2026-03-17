package com.example.healthconnect.ui.mapper

import com.example.healthconnect.domain.LibraryRecords
import org.junit.Test

class RecordTypeNameMapperTest {

    private val mapper = RecordTypeNameMapper()

    @Test
    fun allLibraryRecordsHaveNameResource() {
        val allTypes = LibraryRecords.instantaneous + LibraryRecords.interval + LibraryRecords.series
        allTypes.forEach { type ->
            mapper.nameRes(type) // throws if missing
        }
    }
}