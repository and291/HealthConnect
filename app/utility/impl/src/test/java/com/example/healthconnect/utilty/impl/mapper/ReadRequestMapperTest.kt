package com.example.healthconnect.utilty.impl.mapper

import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.DataOrigin
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.data.mapper.ReadRequestMapper
import com.example.healthconnect.utilty.impl.data.mapper.TypeMapper
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import org.junit.Assert
import org.junit.Test
import java.time.Instant

class ReadRequestMapperTest {

    private val mapper = ReadRequestMapper(TypeMapper())

    private fun baseRequest() = ReadRequest(
        modelType = Steps::class,
        endTime = Instant.parse("2024-01-01T00:00:00Z"),
    )

    @Test
    fun nullPageToken_producesRequestWithNullPageToken() {
        val result = mapper.map<StepsRecord, Steps>(baseRequest(), pageToken = null)

        Assert.assertNull(result.pageToken)
    }

    @Test
    fun nonNullPageToken_isForwardedToRequest() {
        val result = mapper.map<StepsRecord, Steps>(baseRequest(), pageToken = "page-abc")

        Assert.assertEquals("page-abc", result.pageToken)
    }

    @Test
    fun dataOriginPackageNames_areMappedToDataOriginObjects() {
        val request = baseRequest().copy(
            dataOriginFilterPackageName = setOf("com.example.app", "com.other.app")
        )

        val result = mapper.map<StepsRecord, Steps>(request)

        Assert.assertEquals(
            setOf(DataOrigin("com.example.app"), DataOrigin("com.other.app")),
            result.dataOriginFilter,
        )
    }

    @Test
    fun emptyDataOriginFilter_producesEmptySet() {
        val request = baseRequest().copy(dataOriginFilterPackageName = emptySet())

        val result = mapper.map<StepsRecord, Steps>(request)

        Assert.assertEquals(emptySet<DataOrigin>(), result.dataOriginFilter)
    }

    @Test
    fun ascendingOrder_isForwardedToRequest() {
        val request = baseRequest().copy(ascendingOrder = false)

        val result = mapper.map<StepsRecord, Steps>(request)

        Assert.assertEquals(false, result.ascendingOrder)
    }

    @Test
    fun pageSize_isForwardedToRequest() {
        val request = baseRequest().copy(pageSize = 250)

        val result = mapper.map<StepsRecord, Steps>(request)

        Assert.assertEquals(250, result.pageSize)
    }

    @Test
    fun modelType_isMappedToCorrespondingRecordType() {
        val result = mapper.map<StepsRecord, Steps>(baseRequest())

        Assert.assertEquals(StepsRecord::class, result.recordType)
    }
}
