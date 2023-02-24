package bdd

import io.cucumber.java.Scenario
import io.cucumber.plugin.event.TestCase


fun Scenario.screenshotName(): String {
    return "${
        uri.toString().substringAfterLast('/').substringBefore(".feature")
    }-${line}.png"
}

fun TestCase.screenshotName(): String {
    return "${
        uri.toString().substringAfterLast('/').substringBefore(".feature")
    }-${line}.png"
}

fun TestCase.testrailId(): Long? {
    return uri.toString().substringAfterLast('/').substringBefore(".feature").replace("C([0-9]+)".toRegex(), "$1")
        .toLongOrNull()
}
