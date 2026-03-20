package com.example.healthconnect.utilty.impl.ui.mapper

import com.example.healthconnect.utilty.impl.domain.SupportedRecords
import org.junit.Test

class RecordTypeIconMapperTest {

    private val mapper = RecordTypeIconMapper()

    @Test
    fun allLibraryRecordsHaveIcon() {
        val allTypes = SupportedRecords.instantaneous + SupportedRecords.interval + SupportedRecords.series
        allTypes.forEach { type ->
            mapper.icon(type) // throws if missing
        }
    }
}