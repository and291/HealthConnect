package com.example.healthconnect.utilty.impl.domain

import org.junit.Assert
import org.junit.Test

class SupportedModelsTest {

    @Test
    fun recordsAreUnique() {
        val availableTypes = SupportedModels.instantaneous + SupportedModels.interval + SupportedModels.series

        // Ensure there are no duplicates in the combined list
        val recordTypeCounts = availableTypes.groupingBy { it }.eachCount()
        for ((recordType, count) in recordTypeCounts) {
            Assert.assertEquals(
                "The class $recordType appears more than once in the combined list of record types.",
                1,
                count
            )
        }
    }
}