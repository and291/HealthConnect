package com.example.healthconnect.utilty.impl.data.repository

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.editor.api.domain.record.factory.ModelFactory
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.data.mapper.FlowResultMapper
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class PageIteratorTest {

    // region helpers

    /** Fake [Record]: only [Metadata] is declared; it is never accessed in these tests. */
    private fun fakeRecord(): Record = object : Record {
        override val metadata: Metadata get() = error("not expected in tests")
    }

    private fun stepsModel(id: String = UUID.randomUUID().toString()): Steps = Steps(
        metadata = MetadataField(
            recordingMethod = SelectorField(0, SelectorField.Type.RecordingMethod()),
            id = StringField(id, StringField.Type.MetadataId()),
            dataOriginPackageName = StringField("", StringField.Type.MetadataDataOrigin()),
            lastModifiedTime = StringField("", StringField.Type.MetadataLastModifiedTime()),
            clientRecordId = StringField("", StringField.Type.MetadataClientRecordId()),
            clientRecordVersion = StringField("", StringField.Type.MetadataClientRecordVersion()),
        ),
        time = TimeField.Interval(
            startTime = Instant.EPOCH,
            startZoneOffset = null,
            endTime = Instant.EPOCH.plusSeconds(3600),
            endZoneOffset = null,
        ),
        count = ValueField.Lng("1000", ValueField.Type.StepsCount()),
    )

    private fun response(
        records: List<Record> = emptyList(),
        pageToken: String? = null,
    ): ReadRecordsResponse<Record> = ReadRecordsResponse(records, pageToken)

    /** A [ModelFactory] that fails if [ModelFactory.create] is called — used when records are always empty. */
    private val noopFactory = object : ModelFactory {
        override fun create(record: Record): Model = error("unexpected: factory should not be called with empty records")
        override fun createByModel(model: Model): Record = error("not expected")
    }

    private fun TestScope.createPager(
        readPage: suspend (pageToken: String?) -> ReadRecordsResponse<Record>,
        modelFactory: ModelFactory = noopFactory,
        startWithFirstPage: Boolean = true,
    ) = PageIterator(
        readPage = readPage,
        flowResultMapper = FlowResultMapper(),
        modelFactory = modelFactory,
        requiredPermission = "",
        dispatcher = UnconfinedTestDispatcher(testScheduler),
        startWithFirstPage = startWithFirstPage,
    )

    // endregion

    // region First page

    @Test
    fun firstPage_isLoadedAutomatically_withoutRequestNextPage() = runTest {
        val pager = createPager(readPage = { response() })

        val result = pager.pages.first()

        assertTrue(result is FlowResult.Data)
    }

    @Test
    fun firstPage_hasNextPage_trueWhenTokenPresent() = runTest {
        val pager = createPager(readPage = { response(pageToken = "token1") })

        val page = (pager.pages.first() as FlowResult.Data<*>).item as Page

        assertTrue(page.hasNextPage)
    }

    @Test
    fun firstPage_hasNextPage_falseWhenNoToken() = runTest {
        val pager = createPager(readPage = { response() })

        val page = (pager.pages.first() as FlowResult.Data<*>).item as Page

        assertFalse(page.hasNextPage)
    }

    @Test
    fun pageItems_areProducedByModelFactory_onePerRecord() = runTest {
        val record1 = fakeRecord()
        val record2 = fakeRecord()
        val model1 = stepsModel()
        val model2 = stepsModel()
        val recordToModel = mapOf(record1 to model1, record2 to model2)
        val factory = object : ModelFactory {
            override fun create(record: Record): Model = recordToModel.getValue(record)
            override fun createByModel(model: Model): Record = error("not expected")
        }
        val pager = createPager(
            readPage = { response(records = listOf(record1, record2)) },
            modelFactory = factory,
        )

        val page = (pager.pages.first() as FlowResult.Data<*>).item as Page

        assertEquals(listOf(model1, model2), page.items)
    }

    // endregion

    // region Multi-page

    @Test
    fun secondPage_requiresExplicitRequestNextPage() = runTest {
        val pages = mutableListOf<FlowResult<Page>>()
        val pager = createPager(
            readPage = { token ->
                when (token) {
                    null -> response(pageToken = "token1")
                    else -> response()
                }
            }
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            pager.pages.collect { pages.add(it) }
        }

        assertEquals(1, pages.size) // first page arrives automatically
        pager.requestNextPage()
        assertEquals(2, pages.size) // second page only after explicit request
    }

    @Test
    fun secondPage_receivesTokenFromFirstPageResponse() = runTest {
        var receivedToken: String? = "not-yet-set"
        val pager = createPager(
            readPage = { token ->
                when (token) {
                    null -> response(pageToken = "expected-token")
                    else -> {
                        receivedToken = token
                        response()
                    }
                }
            }
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            pager.pages.collect {}
        }
        pager.requestNextPage()

        assertEquals("expected-token", receivedToken)
    }

    @Test
    fun flowCompletesAfterLastPage() = runTest {
        val pager = createPager(readPage = { response() })

        val allPages = pager.pages.toList()

        assertEquals(1, allPages.size)
    }

    // endregion

    // region Error handling

    @Test
    fun errorInReadPage_emitsTerminalResult() = runTest {
        val pager = createPager(readPage = { throw RuntimeException("network error") })

        val result = pager.pages.first()

        assertTrue(result is FlowResult.Terminal.UnhandledException)
    }

    @Test
    fun errorInReadPage_terminalContainsOriginalException() = runTest {
        val cause = RuntimeException("original cause")
        val pager = createPager(readPage = { throw cause })

        val terminal = pager.pages.first() as FlowResult.Terminal.UnhandledException

        assertSame(cause, terminal.throwable)
    }

    // endregion

    // region CONFLATED channel

    @Test
    fun conflated_multipleRequestsBeforeCollection_coalescedIntoOne() = runTest {
        var callCount = 0
        val pager = createPager(
            readPage = { _ ->
                callCount++
                response()
            },
            startWithFirstPage = false,
        )

        repeat(5) { pager.requestNextPage() }
        pager.pages.toList()

        assertEquals("CONFLATED channel must coalesce multiple pending requests into one", 1, callCount)
    }

    // endregion
}
