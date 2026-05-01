package com.example.healthconnect.permissions.impl.ui.screen

import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionRequest
import com.example.healthconnect.permissions.api.domain.PermissionResult
import com.example.healthconnect.permissions.api.domain.PermissionStatus
import com.example.healthconnect.permissions.api.domain.PermissionType
import com.example.healthconnect.permissions.api.usecase.PermissionCoordinator
import com.example.healthconnect.permissions.impl.ui.mapper.PermissionNameMapper
import com.example.healthconnect.permissions.impl.ui.screen.PermissionsViewModel.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PermissionsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val readSteps = HealthPermission("android.permission.health.READ_STEPS", PermissionType.Read)
    private val writeSteps = HealthPermission("android.permission.health.WRITE_STEPS", PermissionType.Write)
    private val readHeartRate = HealthPermission("android.permission.health.READ_HEART_RATE", PermissionType.Read)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region helpers

    /**
     * Activates [PermissionsViewModel.state] upstream so that [StateFlow.value] reflects live data.
     * Required because [kotlinx.coroutines.flow.SharingStarted.WhileSubscribed] only runs the
     * upstream while there is at least one subscriber.
     */
    private fun TestScope.collectState(viewModel: PermissionsViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect {}
        }
    }

    private open class FakePermissionCoordinator(
        private val statusesToReturn: List<PermissionStatus> = emptyList(),
        initialPendingRequest: Set<String>? = null,
    ) : PermissionCoordinator {

        private val _pendingRequest = MutableStateFlow(initialPendingRequest)
        override val pendingRequest: StateFlow<Set<String>?> = _pendingRequest

        private val _results = MutableSharedFlow<PermissionResult>(extraBufferCapacity = 1)
        override val results: SharedFlow<PermissionResult> = _results

        var requestCallCount = 0
            private set
        var lastRequest: PermissionRequest? = null
            private set

        override suspend fun request(request: PermissionRequest) {
            requestCallCount++
            lastRequest = request
            _pendingRequest.value = request.toSet().map { it.permissionString }.toSet()
        }

        override fun onActivityResult(grantedPermissionStrings: Set<String>) = Unit

        override suspend fun getPermissionStatuses(): List<PermissionStatus> = statusesToReturn

        fun emitResult(result: PermissionResult) {
            _results.tryEmit(result)
            _pendingRequest.value = null
        }
    }

    private fun viewModel(coordinator: FakePermissionCoordinator) =
        PermissionsViewModel(coordinator, PermissionNameMapper())

    // endregion

    @Test
    fun `init loadsStatuses`() = runTest(testDispatcher) {
        val statuses = listOf(
            PermissionStatus(readSteps, isGranted = true),
            PermissionStatus(writeSteps, isGranted = false),
        )
        val coordinator = FakePermissionCoordinator(statusesToReturn = statuses)
        val vm = viewModel(coordinator)
        collectState(vm)

        val state = vm.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.readPermissions.size)
        assertEquals(1, state.writePermissions.size)
        assertEquals(readSteps, state.readPermissions.first().status.permission)
        assertEquals(writeSteps, state.writePermissions.first().status.permission)
    }

    @Test
    fun `onRefresh reloadsStatuses`() = runTest(testDispatcher) {
        val coordinator = FakePermissionCoordinator(
            statusesToReturn = listOf(PermissionStatus(readSteps, isGranted = true)),
        )
        val vm = viewModel(coordinator)
        collectState(vm)

        vm.onEvent(Event.Refresh)

        assertFalse(vm.state.value.isLoading)
        assertEquals(1, vm.state.value.readPermissions.size)
    }

    @Test
    fun `onRequestPermission callsCoordinatorRequest`() = runTest(testDispatcher) {
        val coordinator = FakePermissionCoordinator()
        val vm = viewModel(coordinator)
        collectState(vm)

        vm.onEvent(Event.RequestPermission(readSteps))

        assertEquals(1, coordinator.requestCallCount)
        val request = coordinator.lastRequest
        assertTrue(request is PermissionRequest.Single)
        assertEquals(readSteps, (request as PermissionRequest.Single).permission)
    }

    @Test
    fun `onRequestPermission whenInFlight isNoOp`() = runTest(testDispatcher) {
        val coordinator = FakePermissionCoordinator(initialPendingRequest = setOf("some.permission"))
        val vm = viewModel(coordinator)
        collectState(vm)

        vm.onEvent(Event.RequestPermission(readSteps))

        assertEquals("Should not call request when already in flight", 0, coordinator.requestCallCount)
    }

    @Test
    fun `onRequestAllMissing withDeniedPermissions requestsAllDenied`() = runTest(testDispatcher) {
        val statuses = listOf(
            PermissionStatus(readSteps, isGranted = false),
            PermissionStatus(writeSteps, isGranted = true),
            PermissionStatus(readHeartRate, isGranted = false),
        )
        val coordinator = FakePermissionCoordinator(statusesToReturn = statuses)
        val vm = viewModel(coordinator)
        collectState(vm)

        vm.onEvent(Event.RequestAllMissing)

        assertEquals(1, coordinator.requestCallCount)
        val request = coordinator.lastRequest
        assertTrue(request is PermissionRequest.Multiple)
        assertEquals(
            setOf(readSteps, readHeartRate),
            (request as PermissionRequest.Multiple).permissions,
        )
    }

    @Test
    fun `onRequestAllMissing whenAllGranted doesNotCallCoordinator`() = runTest(testDispatcher) {
        val statuses = listOf(
            PermissionStatus(readSteps, isGranted = true),
            PermissionStatus(writeSteps, isGranted = true),
        )
        val coordinator = FakePermissionCoordinator(statusesToReturn = statuses)
        val vm = viewModel(coordinator)
        collectState(vm)

        vm.onEvent(Event.RequestAllMissing)

        assertEquals("Should not call request when all are granted", 0, coordinator.requestCallCount)
    }

    @Test
    fun `afterCoordinatorResultEmitted statusesAreReloaded`() = runTest(testDispatcher) {
        var callCount = 0
        val coordinator = object : FakePermissionCoordinator() {
            override suspend fun getPermissionStatuses(): List<PermissionStatus> {
                callCount++
                return if (callCount == 1) {
                    listOf(PermissionStatus(readSteps, isGranted = false))
                } else {
                    listOf(PermissionStatus(readSteps, isGranted = true))
                }
            }
        }
        val vm = viewModel(coordinator)
        collectState(vm)
        assertEquals(1, callCount)

        vm.onEvent(Event.RequestPermission(readSteps))
        coordinator.emitResult(PermissionResult.AllGranted(setOf(readSteps)))

        assertEquals("Expected reload after result", 2, callCount)
        assertTrue(vm.state.value.readPermissions.first().status.isGranted)
    }

    @Test
    fun `hasAnyDenied isTrueWhenAtLeastOneDenied`() = runTest(testDispatcher) {
        val statuses = listOf(
            PermissionStatus(readSteps, isGranted = true),
            PermissionStatus(writeSteps, isGranted = false),
        )
        val coordinator = FakePermissionCoordinator(statusesToReturn = statuses)
        val vm = viewModel(coordinator)
        collectState(vm)
        assertTrue(vm.state.value.hasAnyDenied)
    }

    @Test
    fun `hasAnyDenied isFalseWhenAllGranted`() = runTest(testDispatcher) {
        val statuses = listOf(
            PermissionStatus(readSteps, isGranted = true),
            PermissionStatus(writeSteps, isGranted = true),
        )
        val coordinator = FakePermissionCoordinator(statusesToReturn = statuses)
        val vm = viewModel(coordinator)
        collectState(vm)
        assertFalse(vm.state.value.hasAnyDenied)
    }
}
