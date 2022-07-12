package spaceEngineers.controller

import java.io.File

val SCENARIO_DIR = "src/jvmTest/resources/game-saves/"

fun String.unixToWindowsPath(): String {
    return replace("^/mnt/([c-z])/".toRegex()) {
        "${it.groupValues[1].uppercase()}:/"
    }
}

fun String.processHomeDir(): String {
    return replace("~", System.getProperty("user.home"))
}

fun File.processHomeDir(): File {
    return File(absolutePath.processHomeDir())
}

fun String.toFile(): File {
    return File(processHomeDir())
}

fun Session.loadFromTestResources(scenarioId: String, scenarioDir: String = SCENARIO_DIR) {
    val file = File(scenarioDir, scenarioId)
    val unixPath = file.absolutePath.unixToWindowsPath()
    check(
        file.exists() ||
                unixPath.toFile().exists()
    ) {
        "Couldn't find scenario"
    }
    loadScenario(unixPath)
}
