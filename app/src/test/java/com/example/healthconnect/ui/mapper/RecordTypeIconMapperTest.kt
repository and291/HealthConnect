package com.example.healthconnect.ui.mapper

import com.example.healthconnect.domain.LibraryRecords
import org.junit.Test

class RecordTypeIconMapperTest {

    private val mapper = RecordTypeIconMapper()

    @Test
    fun allLibraryRecordsHaveIcon() {
        val allTypes = LibraryRecords.instantaneous + LibraryRecords.interval + LibraryRecords.series
        allTypes.forEach { type ->
            mapper.icon(type) // throws if missing
        }
    }
}