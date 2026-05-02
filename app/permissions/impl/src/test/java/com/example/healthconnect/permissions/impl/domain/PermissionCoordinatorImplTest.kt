package com.example.healthconnect.permissions.impl.domain

import com.example.healthconnect.models.api.domain.record.HeartRate
import com.example.healthconnect.models.api.domain.record.Model
import com.example.healthconnect.models.api.domain.record.Steps
import com.example.healthconnect.permissions.api.domain.framework.HealthPermission
import com.example.healthconnect.permissions.api.domain.framework.PermissionRequest
import com.example.healthconnect.permissions.api.domain.framework.PermissionResult
import com.example.healthconnect.permissions.api.domain.framework.usecase.LibraryPermissionResolver
import com.example.healthconnect.permissions.api.domain.framework.usecase.PermissionController
import kotlin.reflect.KClass
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

    private val testRecordTypes = listOf(Steps::class, HeartRate::class)

    private val readSteps = HealthPermission("android.permission.health.READ_STEPS")
    private val writeSteps = HealthPermission("android.permission.health.WRITE_STEPS")
    private val readHeartRate = HealthPermission("android.permission.health.READ_HEART_RATE")

    // region helpers

    private val fakeResolver = object : LibraryPermissionResolver {
        override fun readPermission(type: KClass<out Model>): HealthPermission =
            HealthPermission("read.${type.simpleName}")
        override fun writePermission(type: KClass<out Model>): HealthPermission =
            HealthPermission("write.${type.simpleName}")
    }

    private fun coordinator(grantedOnQuery: Set<String> = emptySet()): PermissionCoordinatorImpl {
        val fakeController = object : PermissionController {
            override suspend fun getGrantedPermissions(): Set<String> = grantedOnQuery
        }
        return PermissionCoordinatorImpl(fakeController, fakeResolver, testRecordTypes)
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
        val grantedString = fakeResolver.readPermission(Steps::class).permissionString
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
        assertEquals(testRecordTypes.size * 2, statuses.size)
    }
}
