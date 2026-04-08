package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import com.example.healthconnect.editor.api.domain.record.Model
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.entity.ReadRequest
import com.example.healthconnect.utilty.impl.domain.usecase.Count
import com.example.healthconnect.utilty.impl.domain.usecase.FlowResult
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
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

    private fun createViewModel(
        countForType: (KClass<out Record>) -> Flow<FlowResult<Int>> = { error("not expected") },
    ): DashboardViewModel {
        val repository = object : LibraryRepository {
            override fun getSdkStatus() = 0
            override suspend fun getGrantedPermissions() = emptySet<String>()
            override suspend fun updateRecords(records: List<Record>) = Unit
            override suspend fun insertRecords(records: List<Record>): InsertRecordsResponse = error("not expected")
            override suspend fun <T : Record> readRecords(request: ReadRecordsRequest<T>): ReadRecordsResponse<T> = error("not expected")
            override suspend fun removeRecord(recordType: KClass<out Record>, metadataId: String) = Unit
            override fun <R : Record> readAll(request: ReadRequest<R>): Flow<FlowResult<Model>> = error("not expected")
            override fun <R : Record> count(request: ReadRequest<R>): Flow<FlowResult<Int>> = countForType(request.recordType)
        }
        return DashboardViewModel(
            count = Count(repository),
            nameMapper = RecordTypeNameMapper(),
            iconMapper = RecordTypeIconMapper(),
        )
    }

    private fun DashboardViewModel.State.Data.countForType(type: KClass<*>): Int? =
        segments.flatMap { it.items }.firstOrNull { it.recordType == type }?.count

    @Test
    fun onLibraryDataManagerClick_emitsShowLibraryDataManagerEffect() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(DashboardViewModel.Event.OnLibraryDataManagerClick)

        assertEquals(DashboardViewModel.Effect.ShowLibraryDataManager, viewModel.effect.value)
    }

    @Test
    fun effectConsumed_clearsShowLibraryDataManagerEffect() = runTest {
        val viewModel = createViewModel()
        viewModel.onEvent(DashboardViewModel.Event.OnLibraryDataManagerClick)

        viewModel.effectConsumed(DashboardViewModel.Effect.ShowLibraryDataManager)

        assertNull(viewModel.effect.value)
    }

    @Test
    fun effectConsumed_doesNotClearEffect_whenDifferentEffectIsConsumed() = runTest {
        val viewModel = createViewModel()
        viewModel.onEvent(DashboardViewModel.Event.OnLibraryDataManagerClick)

        viewModel.effectConsumed(DashboardViewModel.Effect.NavigateToRecords(StepsRecord::class, 0))

        assertEquals(DashboardViewModel.Effect.ShowLibraryDataManager, viewModel.effect.value)
    }

    @Test
    fun onTypeClick_emitsNavigateToRecordsEffect() = runTest {
        val viewModel = createViewModel()

        viewModel.onEvent(DashboardViewModel.Event.OnTypeClick(StepsRecord::class, 42))

        val effect = viewModel.effect.value
        assertEquals(DashboardViewModel.Effect.NavigateToRecords(StepsRecord::class, 42), effect)
    }

    @Test
    fun effectConsumed_clearsNavigateToRecordsEffect() = runTest {
        val viewModel = createViewModel()
        viewModel.onEvent(DashboardViewModel.Event.OnTypeClick(StepsRecord::class, 42))

        viewModel.effectConsumed(DashboardViewModel.Effect.NavigateToRecords(StepsRecord::class, 42))

        assertNull(viewModel.effect.value)
    }

    @Test
    fun effectConsumed_doesNotClearNavigateToRecords_whenShowLibraryDataManagerConsumed() = runTest {
        val viewModel = createViewModel()
        viewModel.onEvent(DashboardViewModel.Event.OnTypeClick(StepsRecord::class, 42))

        viewModel.effectConsumed(DashboardViewModel.Effect.ShowLibraryDataManager)

        assertEquals(
            DashboardViewModel.Effect.NavigateToRecords(StepsRecord::class, 42),
            viewModel.effect.value
        )
    }

    @Test
    fun onRefresh_transitionsStateToData() = runTest {
        val viewModel = createViewModel(countForType = { flowOf(FlowResult.Data(0)) })

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        assertTrue(viewModel.state is DashboardViewModel.State.Data)
    }

    @Test
    fun onRefresh_accumulatesPaginatedCountsForType() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == StepsRecord::class) flow {
                emit(FlowResult.Data(3))
                emit(FlowResult.Data(2))
            } else flowOf(FlowResult.Data(0))
        })

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state as DashboardViewModel.State.Data
        assertEquals(5, state.countForType(StepsRecord::class))
    }

    @Test
    fun onRefresh_withPermissionRequired_setsNullCountForType() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == StepsRecord::class) flowOf(FlowResult.Terminal.PermissionRequired("hc.permission.STEPS"))
            else flowOf(FlowResult.Data(0))
        })

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state as DashboardViewModel.State.Data
        assertNull(state.countForType(StepsRecord::class))
    }

    @Test
    fun onRefresh_withTerminalAfterData_setsNullCountForType() = runTest {
        val viewModel = createViewModel(countForType = { type ->
            if (type == StepsRecord::class) flow {
                emit(FlowResult.Data(3))
                emit(FlowResult.Terminal.UnhandledException(RuntimeException("fail")))
            } else flowOf(FlowResult.Data(0))
        })

        viewModel.onEvent(DashboardViewModel.Event.Refresh)

        val state = viewModel.state as DashboardViewModel.State.Data
        assertNull(state.countForType(StepsRecord::class))
    }
}
