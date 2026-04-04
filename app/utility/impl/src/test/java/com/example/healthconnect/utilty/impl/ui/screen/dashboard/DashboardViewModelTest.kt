package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import com.example.healthconnect.utilty.impl.domain.LibraryRepository
import com.example.healthconnect.utilty.impl.domain.PayloadMapper
import com.example.healthconnect.utilty.impl.domain.ResultMapper
import com.example.healthconnect.utilty.impl.domain.usecase.Read
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeIconMapper
import com.example.healthconnect.utilty.impl.ui.mapper.RecordTypeNameMapper
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

    private fun createViewModel(): DashboardViewModel {
        val repository = object : LibraryRepository {
            override fun getSdkStatus() = 0
            override suspend fun getGrantedPermissions() = emptySet<String>()
            override suspend fun updateRecords(records: List<Record>) = Unit
            override suspend fun insertRecords(records: List<Record>): InsertRecordsResponse = error("not expected")
            override suspend fun <T : Record> readRecords(request: ReadRecordsRequest<T>): ReadRecordsResponse<T> = error("not expected")
            override suspend fun removeRecord(recordType: KClass<out Record>, metadataId: String) = Unit
        }
        return DashboardViewModel(
            read = Read(repository, ResultMapper(), PayloadMapper()),
            nameMapper = RecordTypeNameMapper(),
            iconMapper = RecordTypeIconMapper(),
        )
    }

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
}
