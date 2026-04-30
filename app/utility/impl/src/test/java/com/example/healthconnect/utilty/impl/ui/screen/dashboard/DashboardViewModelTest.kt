package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.Pager
import com.example.healthconnect.utilty.impl.domain.entity.ReadParams
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.ui.mapper.FlowResultTerminalIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapper
import com.example.healthconnect.utilty.impl.ui.screen.dashboard.model.DashboardItem
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Suppress()
    /**
     * Default [countForType] returns [FlowResult.Data] with 0 so the background [init] refresh
     * that starts in [DashboardViewModel.init] always completes cleanly for tests that don't
     * care about count values. Previously it defaulted to `error(...)`, which silently crashed
     * the background coroutine (swallowed by the supervisor job) in every test.
     */
    private fun createViewModel(
        countForType: (KClass<out Model>) -> Flow<FlowResult<Int>> = { flowOf(FlowResult.Data(0)) },
    ): DashboardViewModel {
        val repository = object : LibraryRepository {
            override fun getSdkStatus() = 0
            override suspend fun getGrantedPermissions() = emptySet<String>()
            override suspend fun updateRecords(records: List<Model>) = Unit
            override suspend fun insertRecords(records: List<Model>): List<String> = error("not expected")
            override suspend fun removeRecord(recordType: KClass<out Model>, metadataId: String) = Unit
            override fun <M : Model> pager(params: ReadParams<M>): Pager = error("not expected")
            override fun <M : Model> count(params: ReadParams<M>): Flow<FlowResult<Int>> = countForType(params.modelType)
        }
        return DashboardViewModel(
            count = Count(repository),
            nameMapper = RecordTypeNameMapper(),
            iconMapper = RecordTypeIconMapper(),
            flowResultTerminalIconMapper = FlowResultTerminalIconMapper(),
        )
    }

    /**
     * Returns the count for [type] if the item is in [DashboardItem.LoadingState.Counted], null
     * otherwise (i.e. still loading or failed). Traverses [segments] since the count is no longer
     * stored in a flat map on the state.
     */
    private fun DashboardViewModel.State.Data.countRecords(type: KClass<out Model>): Int? =
        segments.flatMap { it.items }
            .find { it.recordType == type }
            ?.let { (it.state as? DashboardItem.LoadingState.Counted)?.count }

    /**
     * Activates the [DashboardViewModel.state] upstream so that [StateFlow.value] reflects live
     * data. Required because [SharingStarted.WhileSubscribed] only runs the upstream while there
     * is at least one subscriber — mirroring what [collectAsStateWithLifecycle] does in production.
     */
    private fun TestScope.collectState(viewModel: DashboardViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
    }

    // region Effects

    @Test
    fun onLibraryDataManagerClick_emitsShowLibraryDataManagerEffect() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(DashboardViewModel.Event.OnLibraryDataManagerClick)

        assertEquals(DashboardViewModel.Effect.ShowLibraryDataManager, viewModel.effect.first())
    }

    @Test
    fun onTypeClick_emitsNavigateToRecordsEffect() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(DashboardViewModel.Event.OnTypeClick(Steps::class, 42))

        assertEquals(
            DashboardViewModel.Effect.NavigateToRecords(Steps::class, 42),
            viewModel.effect.first(),
        )
    }

    // endregion

    // region State

    @Test
    fun init_startsInDataState() = runTest {
        val viewModel = createViewModel()
        collectState(viewModel)

        assertTrue(viewModel.state.value is DashboardViewModel.State.Data)
    }

    @Test
    fun onRefresh_loadsCountRecords() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == Steps::class) flowOf(FlowResult.Data(42))
            else flowOf(FlowResult.Data(0))
        })
        collectState(viewModel)

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state.value as DashboardViewModel.State.Data
        assertEquals(42, state.countRecords(Steps::class))
    }

    @Test
    fun onRefresh_withUnpermittedAccess_setsNullCountRecords() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == Steps::class) flowOf(FlowResult.Terminal.UnpermittedAccess(SecurityException("no permission")))
            else flowOf(FlowResult.Data(0))
        })
        collectState(viewModel)

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state.value as DashboardViewModel.State.Data
        assertNull(state.countRecords(Steps::class))
    }

    @Test
    fun onRefresh_withTerminalAfterData_setsNullCountRecords() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == Steps::class) flow {
                emit(FlowResult.Data(3))
                emit(FlowResult.Terminal.UnhandledException(RuntimeException("fail")))
            } else flowOf(FlowResult.Data(0))
        })
        collectState(viewModel)

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state.value as DashboardViewModel.State.Data
        assertNull(state.countRecords(Steps::class))
    }

    @Test
    fun onRefresh_whenInDataState_doesNotTransitionAwayFromData() = runTest {
        val blockCounts = CompletableDeferred<Unit>()
        var firstRefreshDone = false
        val viewModel = createViewModel(countForType = {
            flow {
                if (firstRefreshDone) blockCounts.await()
                emit(FlowResult.Data(0))
            }
        })
        collectState(viewModel)
        viewModel.onEvent(DashboardViewModel.Event.Refresh)
        firstRefreshDone = true

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        assertTrue(viewModel.state.value is DashboardViewModel.State.Data)
        blockCounts.complete(Unit)
    }

    @Test
    fun onRefresh_whenInDataState_setsIsRefreshingTrueBeforeCountsLoad() = runTest {
        val blockCounts = CompletableDeferred<Unit>()
        var firstRefreshDone = false
        val viewModel = createViewModel(countForType = {
            flow {
                if (firstRefreshDone) blockCounts.await()
                emit(FlowResult.Data(0))
            }
        })
        collectState(viewModel)
        viewModel.onEvent(DashboardViewModel.Event.Refresh)
        firstRefreshDone = true

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        assertTrue((viewModel.state.value as DashboardViewModel.State.Data).isRefreshing)
        blockCounts.complete(Unit)
        assertFalse((viewModel.state.value as DashboardViewModel.State.Data).isRefreshing)
    }

    // endregion
}
