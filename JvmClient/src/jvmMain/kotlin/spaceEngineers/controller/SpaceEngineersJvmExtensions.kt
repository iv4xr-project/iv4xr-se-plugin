package spaceEngineers.controller

import spaceEngineers.iv4xr.navigation.Iv4XRAStarPathFinder
import spaceEngineers.model.BlockId
import spaceEngineers.model.Vec3F
import spaceEngineers.navigation.PathFinder
import java.io.File
import java.util.*

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

fun SpaceEngineers.extend(pathFinder: PathFinder<BlockId, Vec3F, String, String> = Iv4XRAStarPathFinder()): ExtendedSpaceEngineers {
    if (this is ExtendedSpaceEngineers) {
        return this
    }
    return DataExtendedSpaceEngineers(this, pathFinder = pathFinder)
}

fun Observer.downloadScreenShot(destination: File) {
    destination.parentFile.mkdirs()
    destination.writeBytes(Base64.getDecoder().decode(downloadScreenshotBase64()))
}
