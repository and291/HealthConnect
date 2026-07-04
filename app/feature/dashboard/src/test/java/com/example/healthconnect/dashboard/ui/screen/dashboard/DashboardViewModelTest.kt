package com.example.healthconnect.dashboard.ui.screen.dashboard

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.healthconnect.dashboard.api.domain.entity.CountResult
import com.example.healthconnect.dashboard.api.domain.entity.DashboardCategory
import com.example.healthconnect.dashboard.api.domain.entity.DashboardType
import com.example.healthconnect.dashboard.api.domain.usecase.CountRecords
import com.example.healthconnect.dashboard.api.domain.usecase.GetDashboardCatalog
import com.example.healthconnect.dashboard.ui.screen.dashboard.model.DashboardItem
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

    // Marker type tokens standing in for real record types; the ViewModel only uses them as keys.
    private class TypeA
    private class TypeB

    private val icon: ImageVector = ImageVector.Builder(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).build()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Default [countForType] returns [CountResult.Counted] with 0 so the background [init] refresh
     * that starts in [DashboardViewModel.init] always completes cleanly for tests that don't
     * care about count values.
     */
    private fun createViewModel(
        countForType: (KClass<*>) -> Flow<CountResult> = { flowOf(CountResult.Counted(0)) },
    ): DashboardViewModel {
        val catalog = object : GetDashboardCatalog {
            override fun invoke(): List<DashboardCategory> = listOf(
                DashboardCategory(
                    titleRes = android.R.string.untitled,
                    types = listOf(
                        DashboardType(TypeA::class, android.R.string.untitled, icon),
                        DashboardType(TypeB::class, android.R.string.untitled, icon),
                    ),
                )
            )
        }
        val countRecords = object : CountRecords {
            override fun invoke(type: KClass<*>): Flow<CountResult> = countForType(type)
        }
        return DashboardViewModel(getCatalog = catalog, countRecords = countRecords)
    }

    /**
     * Returns the count for [type] if the item is in [DashboardItem.LoadingState.Counted], null
     * otherwise (i.e. still loading or failed). Traverses [segments] since the count is not stored
     * in a flat map on the state.
     */
    private fun DashboardViewModel.State.Data.countRecords(type: KClass<*>): Int? =
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

        viewModel.onEvent(DashboardViewModel.Event.OnTypeClick(TypeA::class, 42))

        assertEquals(
            DashboardViewModel.Effect.NavigateToRecords(TypeA::class, 42),
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
            if (type == TypeA::class) flowOf(CountResult.Counted(42))
            else flowOf(CountResult.Counted(0))
        })
        collectState(viewModel)

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state.value as DashboardViewModel.State.Data
        assertEquals(42, state.countRecords(TypeA::class))
    }

    @Test
    fun onRefresh_withFailedCount_setsNullCountRecords() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == TypeA::class) flowOf(CountResult.Failed(icon))
            else flowOf(CountResult.Counted(0))
        })
        collectState(viewModel)

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state.value as DashboardViewModel.State.Data
        assertNull(state.countRecords(TypeA::class))
    }

    @Test
    fun onRefresh_withFailedAfterData_setsNullCountRecords() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == TypeA::class) flow {
                emit(CountResult.Counted(3))
                emit(CountResult.Failed(icon))
            } else flowOf(CountResult.Counted(0))
        })
        collectState(viewModel)

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state.value as DashboardViewModel.State.Data
        assertNull(state.countRecords(TypeA::class))
    }

    @Test
    fun onRefresh_whenInDataState_doesNotTransitionAwayFromData() = runTest {
        val blockCounts = CompletableDeferred<Unit>()
        var firstRefreshDone = false
        val viewModel = createViewModel(countForType = {
            flow {
                if (firstRefreshDone) blockCounts.await()
                emit(CountResult.Counted(0))
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
                emit(CountResult.Counted(0))
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
