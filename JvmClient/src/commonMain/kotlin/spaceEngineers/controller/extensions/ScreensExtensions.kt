package spaceEngineers.controller.extensions

import spaceEngineers.controller.Screens
import spaceEngineers.model.ScreenName
import spaceEngineers.util.whileWithTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

suspend fun Screens.waitForScreen(
    timeout: Duration = 60_000.milliseconds,
    retryInterval: Duration = 500.milliseconds,
    screenName: ScreenName
) {
    whileWithTimeout(timeout, retryInterval) {
        focusedScreen.data()?.run {
            !(typedName == screenName && isLoaded)
        } ?: true
    }
}

suspend fun Screens.waitForScreenFinish(
    timeout: Duration = 60_000.milliseconds,
    retryInterval: Duration = 500.milliseconds,
    screenName: ScreenName
) {
    whileWithTimeout(timeout, retryInterval) {
        focusedScreen.data()?.typedName == screenName
    }
}

fun Screens.typedFocusedScreen(): ScreenName {
    return focusedScreen.data()?.typedName ?: error("No focused screen!")
}
