package spaceEngineers.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

suspend fun whileWithTimeout(
    timeout: Duration = 10_000.milliseconds,
    waitDelay: Duration = 100.milliseconds,
    condition: () -> Boolean
) {
    withTimeout(timeout) {
        while (condition()) {
            delay(waitDelay)
        }
    }
}

@Deprecated("Use method that passes kotlin.time.Duration")
suspend fun whileWithTimeout(timeout: Long, waitDelay: Long = 100L, condition: () -> Boolean) {
    withTimeout(timeout) {
        while (condition()) {
            delay(waitDelay)
        }
    }
}
