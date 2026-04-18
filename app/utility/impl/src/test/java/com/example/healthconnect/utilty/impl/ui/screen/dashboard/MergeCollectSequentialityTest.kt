package com.example.healthconnect.utilty.impl.ui.screen.dashboard

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

/**
 * Demonstrates that the `collect` lambda of a merged flow is always invoked sequentially,
 * even when source flows emit concurrently from multiple threads via [flowOn].
 *
 * This means a plain [MutableMap] inside a `collect` block is safe without
 * [java.util.concurrent.ConcurrentHashMap], even on [Dispatchers.Default].
 */
class MergeCollectSequentialityTest {

    @Test
    fun `collect lambda is never invoked concurrently even when source flows emit from multiple threads`() =
        runBlocking {
            val activeInCollect = AtomicInteger(0)
            val maxConcurrentInvocations = AtomicInteger(0)

            // Mirror the ViewModel: collect on Dispatchers.Default,
            // each source flow also on Dispatchers.Default — maximum concurrency pressure.
            withContext(Dispatchers.Default) {
                (1..100).map { i ->
                    flow { emit(i) }.flowOn(Dispatchers.Default)
                }.merge().collect {
                    val active = activeInCollect.incrementAndGet()
                    maxConcurrentInvocations.updateAndGet { max(it, active) }

                    // Busy-wait to keep this invocation "alive" while other source
                    // coroutines are emitting, maximising the chance of catching
                    // a concurrent second entry if the Flow contract were violated.
                    val deadline = System.nanoTime() + 100L // 0.1 ms
                    while (System.nanoTime() < deadline) { /* spin */ }

                    activeInCollect.decrementAndGet()
                }
            }

            assertEquals(
                "collect lambda was invoked concurrently — max simultaneous invocations: ${maxConcurrentInvocations.get()}",
                1,
                maxConcurrentInvocations.get(),
            )
        }
}
