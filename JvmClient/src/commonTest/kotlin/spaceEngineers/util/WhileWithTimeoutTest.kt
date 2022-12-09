package spaceEngineers.util

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlin.test.*
import kotlin.time.Duration.Companion.milliseconds

class WhileWithTimeoutTest {
    @Test
    fun `whileWithTimeout returns when condition is met`() {
        runBlocking {
            val timeout = 100.milliseconds
            val waitDelay = 10.milliseconds
            var conditionMet = false
            val condition: suspend (Int) -> Boolean = {
                if (it == 5) {
                    conditionMet = true
                    false
                } else {
                    true
                }
            }

            whileWithTimeout(timeout, waitDelay, condition)

            assertTrue(conditionMet)
        }
    }

    @Test
    fun `whileWithTimeout throws timeout exception when condition is not met within timeout`() {
        runBlocking {
            val timeout = 100.milliseconds
            val waitDelay = 10.milliseconds
            val condition: suspend (Int) -> Boolean = { true }

            assertFailsWith<TimeoutCancellationException> {
                whileWithTimeout(timeout, waitDelay, condition)
            }
        }
    }

    @Test
    fun `whileWithTimeout uses default timeout when none is specified`() {
        runBlocking {
            val waitDelay = 10.milliseconds
            val condition: suspend (Int) -> Boolean = { true }

            assertFailsWith<TimeoutCancellationException> {
                whileWithTimeout(waitDelay = waitDelay, condition = condition)
            }
        }
    }

    @Test
    fun `whileWithTimeout uses default wait delay when none is specified`() {
        runBlocking {
            val timeout = 100.milliseconds
            val condition: suspend (Int) -> Boolean = { true }

            assertFailsWith<TimeoutCancellationException> {
                whileWithTimeout(timeout = timeout, condition = condition)
            }
        }
    }

}