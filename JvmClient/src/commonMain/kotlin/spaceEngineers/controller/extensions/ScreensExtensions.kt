package spaceEngineers.controller.extensions

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import spaceEngineers.controller.Screens

suspend fun Screens.waitForScreen(timeoutMs: Long = 60_000, singleDelay: Long = 500, screenName: String) {
    withTimeout(timeoutMs) {
        while (focusedScreen() != screenName) {
            delay(singleDelay)
        }
    }
}
