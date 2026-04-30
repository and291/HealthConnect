package com.example.healthconnect.permissions.impl.domain

import androidx.health.connect.client.PermissionController
import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionRequest
import com.example.healthconnect.permissions.api.domain.PermissionResult
import com.example.healthconnect.permissions.api.domain.PermissionType
import com.example.healthconnect.permissions.impl.data.AllHealthPermissions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PermissionCoordinatorImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val readSteps = HealthPermission("android.permission.health.READ_STEPS", PermissionType.Read)
    private val writeSteps = HealthPermission("android.permission.health.WRITE_STEPS", PermissionType.Write)
    private val readHeartRate = HealthPermission("android.permission.health.READ_HEART_RATE", PermissionType.Read)

    // region helpers

    private fun coordinator(grantedOnQuery: Set<String> = emptySet()): PermissionCoordinatorImpl {
        val fakeController = object : PermissionController {
            override suspend fun getGrantedPermissions(): Set<String> = grantedOnQuery
            override suspend fun revokeAllPermissions() = Unit
        }
        return PermissionCoordinatorImpl(fakeController)
    }

    // endregion

    @Test
    fun `request whenIdle setsPendingRequest`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        coordinator.request(PermissionRequest.Single(readSteps))
        assertEquals(setOf(readSteps.permissionString), coordinator.pendingRequest.value)
    }

    @Test
    fun `request Multiple setsPendingRequestWithAllStrings`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        coordinator.request(PermissionRequest.Multiple(setOf(readSteps, writeSteps)))
        assertEquals(
            setOf(readSteps.permissionString, writeSteps.permissionString),
            coordinator.pendingRequest.value,
        )
    }

    @Test
    fun `request whenInFlight isNoOp`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        coordinator.request(PermissionRequest.Single(readSteps))
        val firstPending = coordinator.pendingRequest.value

        coordinator.request(PermissionRequest.Single(readHeartRate))

        assertEquals("Second request must not overwrite in-flight request", firstPending, coordinator.pendingRequest.value)
    }

    @Test
    fun `onActivityResult allGranted emitsAllGrantedResult`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        var captured: PermissionResult? = null
        val job = launch { captured = coordinator.results.first() }
        coordinator.request(PermissionRequest.Single(readSteps))
        coordinator.onActivityResult(setOf(readSteps.permissionString))
        job.join()
        assertTrue("Expected AllGranted but got $captured", captured is PermissionResult.AllGranted)
        assertEquals(setOf(readSteps), (captured as PermissionResult.AllGranted).granted)
    }

    @Test
    fun `onActivityResult noneGranted emitsAllDeniedResult`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        var captured: PermissionResult? = null
        val job = launch { captured = coordinator.results.first() }
        coordinator.request(PermissionRequest.Single(readSteps))
        coordinator.onActivityResult(emptySet())
        job.join()
        assertTrue("Expected AllDenied but got $captured", captured is PermissionResult.AllDenied)
        assertEquals(setOf(readSteps), (captured as PermissionResult.AllDenied).denied)
    }

    @Test
    fun `onActivityResult partialGrant emitsSomeGrantedResult`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        var captured: PermissionResult? = null
        val job = launch {
            captured = coordinator.results.first()
        }
        coordinator.request(PermissionRequest.Multiple(setOf(readSteps, writeSteps)))
        coordinator.onActivityResult(setOf(readSteps.permissionString))
        job.join()
        assertTrue("Expected SomeGranted but got $captured", captured is PermissionResult.SomeGranted)
        val someGranted = captured as PermissionResult.SomeGranted
        assertEquals(setOf(readSteps), someGranted.granted)
        assertEquals(setOf(writeSteps), someGranted.denied)
    }

    @Test
    fun `onActivityResult clearsPendingRequest`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        launch { coordinator.results.first() }
        coordinator.request(PermissionRequest.Single(readSteps))
        coordinator.onActivityResult(setOf(readSteps.permissionString))
        assertNull("pendingRequest should be null after result", coordinator.pendingRequest.value)
    }

    @Test
    fun `onActivityResult withNoInFlightRequest isNoOp`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        // Must not throw and must not emit on results
        coordinator.onActivityResult(setOf(readSteps.permissionString))
        assertNull(coordinator.pendingRequest.value)
    }

    @Test
    fun `getPermissionStatuses mapsGrantedPermissionsCorrectly`() = runTest(testDispatcher) {
        val grantedString = AllHealthPermissions.read.first().permissionString
        val coordinator = coordinator(grantedOnQuery = setOf(grantedString))

        val statuses = coordinator.getPermissionStatuses()

        val grantedStatus = statuses.find { it.permission.permissionString == grantedString }
        assertTrue("Expected granted permission to be marked granted", grantedStatus?.isGranted == true)

        val deniedCount = statuses.count { !it.isGranted }
        assertTrue("Expected most permissions to be denied", deniedCount > 0)
    }

    @Test
    fun `getPermissionStatuses returnsAllDeclaredPermissions`() = runTest(testDispatcher) {
        val coordinator = coordinator()
        val statuses = coordinator.getPermissionStatuses()
        assertEquals(AllHealthPermissions.all.size, statuses.size)
    }
}
