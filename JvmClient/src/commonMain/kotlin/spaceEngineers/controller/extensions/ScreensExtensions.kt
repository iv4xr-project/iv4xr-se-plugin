package spaceEngineers.controller.extensions

import spaceEngineers.controller.Screens
import spaceEngineers.util.whileWithTimeout

suspend fun Screens.waitForScreen(timeoutMs: Long = 60_000, singleDelay: Long = 50, screenName: String) {
    whileWithTimeout(timeoutMs, singleDelay) {
        focusedScreen() != screenName
    }
}
