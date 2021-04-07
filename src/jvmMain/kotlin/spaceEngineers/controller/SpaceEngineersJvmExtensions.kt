package spaceEngineers.controller

import java.io.File

val SCENARIO_DIR = "src/jvmTest/resources/game-saves/"

fun Session.loadFromTestResources(scenarioId: String, scenarioDir: String = SCENARIO_DIR) {
    loadScenario(File(scenarioDir, scenarioId).absolutePath)
}