package com.example.healthconnect.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class LibraryRecordsTest {

    @Test
    fun recordsAreUnique() {
        val availableTypes = LibraryRecords.instantaneous + LibraryRecords.interval + LibraryRecords.series

        // Ensure there are no duplicates in the combined list
        val recordTypeCounts = availableTypes.groupingBy { it }.eachCount()
        for ((recordType, count) in recordTypeCounts) {
            assertEquals(
                "The class $recordType appears more than once in the combined list of record types.",
                1,
                count
            )
        }
    }
}