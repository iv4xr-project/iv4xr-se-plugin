package spaceEngineers.controller.extensions

import spaceEngineers.controller.Screens
import spaceEngineers.model.ScreenName
import spaceEngineers.model.toScreenName
import spaceEngineers.util.whileWithTimeout

suspend fun Screens.waitForScreen(timeoutMs: Long = 60_000, singleDelay: Long = 500, screenName: ScreenName) {
    whileWithTimeout(timeoutMs, singleDelay) {
        typedFocusedScreen() != screenName
    }
}

fun Screens.typedFocusedScreen(): ScreenName {
    return focusedScreen().toScreenName()
}
