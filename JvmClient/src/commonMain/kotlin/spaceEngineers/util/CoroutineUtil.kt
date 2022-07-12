package spaceEngineers.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

suspend fun whileWithTimeout(timeout: Long = 10_000L, waitDelay: Long = 100L, condition: () -> Boolean) {
    withTimeout(timeout) {
        while (condition()) {
            delay(waitDelay)
        }
    }
}
