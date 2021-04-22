package spaceEngineers.controller

import spaceEngineers.model.SeBlock
import spaceEngineers.model.allBlocks
import java.io.File

val SCENARIO_DIR = "src/jvmTest/resources/game-saves/"

fun Session.loadFromTestResources(scenarioId: String, scenarioDir: String = SCENARIO_DIR) {
    loadScenario(File(scenarioDir, scenarioId).absolutePath)
}

fun Observer.blockById(blockId: String): SeBlock {
    return observeBlocks().allBlocks.first { it.id == blockId }
}

fun runWhileConditionUntilTimeout(
    timeoutMs: Int = 20000,
    conditionBlock: () -> Boolean
) {
    val initialTime = System.currentTimeMillis()
    while (conditionBlock()) {
        check(System.currentTimeMillis() - initialTime < timeoutMs) {
            "Timeout after ${timeoutMs}ms"
        }
    }
}

fun ContextControllerWrapper.grindDownToPercentage(blockId: String, percentage: Double) {
    val integrity = observer.blockById(blockId).maxIntegrity * percentage / 100.0
    items.equip(context.grinderLocation!!)
    Thread.sleep(500)
    items.beginUsingTool()
    runWhileConditionUntilTimeout {
        observer.blockById(blockId).integrity > integrity
    }
    items.endUsingTool()
}

fun ContextControllerWrapper.torchUpToPercentage(blockId: String, percentage: Double) {

    val integrity = observer.blockById(blockId).maxIntegrity * percentage / 100.0

    items.equip(context.torchLocation!!)
    Thread.sleep(500)

    items.beginUsingTool()
    runWhileConditionUntilTimeout {
        observer.blockById(blockId).integrity < integrity
    }
    items.endUsingTool()
}

fun ContextControllerWrapper.torchBackToMax(blockId: String) {
    torchUpToPercentage(blockId, 100.0)
}
