package com.example.healthconnect.utilty.impl.domain

import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.DataOrigin
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class ReadRequestMapperTest {

    private val mapper = ReadRequestMapper()

    private fun baseRequest() = ReadRequest(
        recordType = StepsRecord::class,
        endTime = Instant.parse("2024-01-01T00:00:00Z"),
    )

    @Test
    fun nullPageToken_producesRequestWithNullPageToken() {
        val result = mapper.map(baseRequest(), pageToken = null)

        assertNull(result.pageToken)
    }

    @Test
    fun nonNullPageToken_isForwardedToRequest() {
        val result = mapper.map(baseRequest(), pageToken = "page-abc")

        assertEquals("page-abc", result.pageToken)
    }

    @Test
    fun dataOriginPackageNames_areMappedToDataOriginObjects() {
        val request = baseRequest().copy(
            dataOriginFilterPackageName = setOf("com.example.app", "com.other.app")
        )

        val result = mapper.map(request)

        assertEquals(
            setOf(DataOrigin("com.example.app"), DataOrigin("com.other.app")),
            result.dataOriginFilter,
        )
    }

    @Test
    fun emptyDataOriginFilter_producesEmptySet() {
        val request = baseRequest().copy(dataOriginFilterPackageName = emptySet())

        val result = mapper.map(request)

        assertEquals(emptySet<DataOrigin>(), result.dataOriginFilter)
    }

    @Test
    fun ascendingOrder_isForwardedToRequest() {
        val request = baseRequest().copy(ascendingOrder = false)

        val result = mapper.map(request)

        assertEquals(false, result.ascendingOrder)
    }

    @Test
    fun pageSize_isForwardedToRequest() {
        val request = baseRequest().copy(pageSize = 250)

        val result = mapper.map(request)

        assertEquals(250, result.pageSize)
    }

    @Test
    fun recordType_isForwardedToRequest() {
        val result = mapper.map(baseRequest())

        assertEquals(StepsRecord::class, result.recordType)
    }
}
