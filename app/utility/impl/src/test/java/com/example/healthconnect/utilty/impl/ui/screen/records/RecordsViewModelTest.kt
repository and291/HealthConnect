package com.example.healthconnect.utilty.impl.ui.screen.records

import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.components.api.domain.entity.field.composite.MetadataField
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.data.mapper.PayloadMapper
import com.example.healthconnect.utilty.impl.data.mapper.ResultMapper
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.Page
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import com.example.healthconnect.utilty.impl.domain.usecase.Delete
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.domain.usecase.ReadAll
import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionRequest
import com.example.healthconnect.permissions.api.domain.PermissionResult
import com.example.healthconnect.permissions.api.domain.PermissionStatus
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import com.example.healthconnect.utilty.impl.domain.mapper.RecordTypePermissionMapper
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.Effect
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.Event
import com.example.healthconnect.utilty.impl.ui.screen.records.RecordsViewModel.State.DisplayPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KClass

@OptIn(ExperimentalCoroutinesApi::class)
class RecordsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val pagers = mutableListOf<FakePager>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        pagers.clear()
    }

    // region helpers

    private open inner class FakePermissionCoordinator : PermissionCoordinator {
        private val _pendingRequest = MutableStateFlow<Set<String>?>(null)
        override val pendingRequest: StateFlow<Set<String>?> = _pendingRequest.asStateFlow()
        private val _results = MutableSharedFlow<PermissionResult>(replay = 0, extraBufferCapacity = 1)
        override val results: SharedFlow<PermissionResult> = _results.asSharedFlow()
        override suspend fun request(request: PermissionRequest) {}
        override fun onActivityResult(grantedPermissionStrings: Set<String>) {}
        override suspend fun getPermissionStatuses(): List<PermissionStatus> = emptyList()
    }

    private class FakePager : Pager {
        private val channel = Channel<FlowResult<Page>>(Channel.UNLIMITED)
        override val pages: Flow<FlowResult<Page>> = channel.receiveAsFlow()
        var requestedNextPageCount = 0
            private set

        override fun requestNextPage() {
            requestedNextPageCount++
        }

        fun sendPage(page: FlowResult<Page>) {
            channel.trySend(page)
        }
    }

    private fun createViewModel(
        onRemoveRecord: suspend () -> Unit = {},
    ): RecordsViewModel {
        val repository = object : LibraryRepository {
            override fun getSdkStatus() = 0
            override suspend fun getGrantedPermissions() = emptySet<String>()
            override suspend fun updateRecords(records: List<Model>) = Unit
            override suspend fun insertRecords(records: List<Model>): List<String> = emptyList()
            override suspend fun removeRecord(recordType: KClass<out Model>, metadataId: String) = onRemoveRecord()
            override fun <M : Model> pager(params: ReadParams<M>): Pager {
                val pager = FakePager()
                pagers.add(pager)
                return pager
            }
            override fun <M : Model> count(params: ReadParams<M>): Flow<FlowResult<Int>> = error("not expected")
        }
        return RecordsViewModel(
            recordType = Steps::class,
            readAll = ReadAll(repository),
            delete = Delete(repository, ResultMapper(), PayloadMapper()),
            coordinator = FakePermissionCoordinator(),
            permissionMapper = RecordTypePermissionMapper(),
        )
    }

    private fun dataPage(vararg models: Model, hasNextPage: Boolean = false): FlowResult.Data<Page> =
        FlowResult.Data(Page(items = models.toList(), hasNextPage = hasNextPage))

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

    // endregion

    // region Initial state

    @Test
    fun initialState_hasNoPages_andIsRefreshing() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.stateFlow.value
        assertTrue(state.isRefreshing)
        assertTrue(state.pages.isEmpty())
        assertFalse(state.hasMorePages)
    }

    @Test
    fun initialState_requestsFirstPager() = runTest {
        createViewModel()

        assertEquals(1, pagers.size)
    }

    // endregion

    // region Page loading

    @Test
    fun afterFirstPageArrives_isNotRefreshing_andRecordsAppear() = runTest {
        val viewModel = createViewModel()
        val model = stepsModel()

        pagers[0].sendPage(dataPage(model))

        val state = viewModel.stateFlow.value
        assertFalse(state.isRefreshing)
        assertEquals(1, state.pages.size)
        val recordPage = state.pages[0] as DisplayPage.Record
        assertSame(model, recordPage.records[0])
    }

    @Test
    fun hasMorePages_trueWhenPageHasNextPage() = runTest {
        val viewModel = createViewModel()

        pagers[0].sendPage(dataPage(stepsModel(), hasNextPage = true))

        assertTrue(viewModel.stateFlow.value.hasMorePages)
    }

    @Test
    fun hasMorePages_falseAfterLastPage() = runTest {
        val viewModel = createViewModel()

        pagers[0].sendPage(dataPage(stepsModel(), hasNextPage = false))

        assertFalse(viewModel.stateFlow.value.hasMorePages)
    }

    @Test
    fun multiplePages_accumulateInOrder() = runTest {
        val viewModel = createViewModel()
        val model1 = stepsModel()
        val model2 = stepsModel()

        pagers[0].sendPage(dataPage(model1, hasNextPage = true))
        pagers[0].sendPage(dataPage(model2, hasNextPage = false))

        val pages = viewModel.stateFlow.value.pages
        assertEquals(2, pages.size)
        assertSame(model1, (pages[0] as DisplayPage.Record).records[0])
        assertSame(model2, (pages[1] as DisplayPage.Record).records[0])
    }

    @Test
    fun terminalResult_addsErrorPageToState() = runTest {
        val viewModel = createViewModel()

        pagers[0].sendPage(FlowResult.Terminal.UnhandledException(RuntimeException("network error")))

        val state = viewModel.stateFlow.value
        assertEquals(1, state.pages.size)
        assertTrue(state.pages[0] is DisplayPage.Error)
    }

    @Test
    fun terminalResult_doesNotSetHasMorePages() = runTest {
        val viewModel = createViewModel()

        pagers[0].sendPage(FlowResult.Terminal.UnhandledException(RuntimeException()))

        assertFalse(viewModel.stateFlow.value.hasMorePages)
    }

    // endregion

    // region NextPage

    @Test
    fun onNextPage_callsRequestNextPageOnCurrentPager() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(Event.NextPage)

        assertEquals(1, pagers[0].requestedNextPageCount)
    }

    // endregion

    // region Refresh

    @Test
    fun onRefresh_createsNewPager_andResetsToRefreshingState() = runTest {
        val viewModel = createViewModel()
        pagers[0].sendPage(dataPage(stepsModel()))

        viewModel.onEvent(Event.Refresh)

        assertEquals(2, pagers.size)
        val state = viewModel.stateFlow.value
        assertTrue(state.isRefreshing)
        assertTrue(state.pages.isEmpty())
    }

    @Test
    fun onRefresh_newPagerPagesAppearInState() = runTest {
        val viewModel = createViewModel()
        pagers[0].sendPage(dataPage(stepsModel()))
        viewModel.onEvent(Event.Refresh)
        val newModel = stepsModel()

        pagers[1].sendPage(dataPage(newModel))

        val records = (viewModel.stateFlow.value.pages[0] as DisplayPage.Record).records
        assertEquals(1, viewModel.stateFlow.value.pages.size)
        assertSame(newModel, records[0])
    }

    @Test
    fun onNextPage_afterRefresh_targetsNewPager() = runTest {
        val viewModel = createViewModel()
        pagers[0].sendPage(dataPage(stepsModel()))
        viewModel.onEvent(Event.Refresh)

        viewModel.onEvent(Event.NextPage)

        assertEquals(0, pagers[0].requestedNextPageCount)
        assertEquals(1, pagers[1].requestedNextPageCount)
    }

    // endregion

    // region Effects

    @Test
    fun onRecordClick_emitsOpenRecordScreenEffect() = runTest {
        val viewModel = createViewModel()
        val model = stepsModel()

        viewModel.onEvent(Event.OnRecordClick(model))

        assertEquals(Effect.OpenRecordScreen(model), viewModel.effect.first())
    }

    // endregion
}
