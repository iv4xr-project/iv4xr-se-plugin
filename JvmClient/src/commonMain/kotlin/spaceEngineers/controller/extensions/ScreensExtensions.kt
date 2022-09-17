package spaceEngineers.controller.extensions

import spaceEngineers.controller.Screens
import spaceEngineers.model.ScreenName
import spaceEngineers.model.toScreenName
import spaceEngineers.util.whileWithTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

suspend fun Screens.waitForScreen(timeoutMs: Long = 60_000, singleDelay: Long = 500, screenName: ScreenName) {
    whileWithTimeout(timeoutMs, singleDelay) {
        typedFocusedScreen() != screenName
    }
}

suspend fun Screens.waitForScreen(
    timeout: Duration = 60_000.milliseconds,
    retryInterval: Duration = 500.milliseconds,
    screenName: ScreenName
) {
    whileWithTimeout(timeout, retryInterval) {
        typedFocusedScreen() != screenName
    }
}

fun Screens.typedFocusedScreen(): ScreenName {
    return focusedScreen.data().name.toScreenName()
}
