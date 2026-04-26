package com.example.healthconnect.utilty.impl.domain.usecase

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.time.Instant
import kotlin.reflect.KClass

class ReadAllTest {

    private var capturedParams: ReadParams<*>? = null

    private val readAll = ReadAll(object : LibraryRepository {
        override fun getSdkStatus() = 0
        override suspend fun getGrantedPermissions() = emptySet<String>()
        override suspend fun updateRecords(records: List<Model>) = Unit
        override suspend fun insertRecords(records: List<Model>): List<String> = emptyList()
        override suspend fun removeRecord(recordType: KClass<out Model>, metadataId: String) = Unit
        override fun <M : Model> pager(params: ReadParams<M>): Pager {
            capturedParams = params
            return object : Pager {
                override val pages: Flow<FlowResult<Page>> = emptyFlow()
                override fun requestNextPage() = Unit
            }
        }
        override fun <M : Model> count(params: ReadParams<M>): Flow<FlowResult<Int>> = error("not expected")
    })

    @Test
    fun invoke_passesCorrectRecordType() {
        readAll(Steps::class)

        assertEquals(Steps::class, capturedParams!!.modelType)
    }

    @Test
    fun invoke_usesDescendingOrder() {
        readAll(Steps::class)

        assertFalse("ReadAll must use descending order so newest records appear first", capturedParams!!.ascendingOrder)
    }

    @Test
    fun invoke_usesPageSizeOf30() {
        readAll(Steps::class)

        assertEquals(30, capturedParams!!.pageSize)
    }

    @Test
    fun invoke_setsEndTimeToApproximatelyNow() {
        val before = Instant.now()
        readAll(Steps::class)
        val after = Instant.now()

        val endTime = capturedParams!!.endTime
        assertFalse("endTime must not be before the call", endTime.isBefore(before))
        assertFalse("endTime must not be after the call", endTime.isAfter(after))
    }
}
