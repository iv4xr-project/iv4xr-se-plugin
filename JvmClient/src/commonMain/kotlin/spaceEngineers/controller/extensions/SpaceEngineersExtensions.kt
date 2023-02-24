package spaceEngineers.controller.extensions

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.Block
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockById

suspend fun SpaceEngineers.grindUntilIntegrityValue(
    block: Block,
    integrity: Double,
    toolLocation: ToolbarLocation,
    checkBlockIntegrity: (Block, Double) -> Boolean = { block_, integrity_ -> block_.integrity > integrity_ },
    timeoutMs: Long = 20000L,
) {
    items.equip(toolLocation)
    delay(500)
    character.beginUsingTool()

    withTimeout(timeoutMs) {
        while (checkBlockIntegrity(observer.blockById(block.id), integrity)) {
            yield()
        }
    }
    character.endUsingTool()
}

suspend fun SpaceEngineers.grindUntilIntegrityPercentage(
    block: Block,
    percentage: Double,
    toolLocation: ToolbarLocation,
    checkBlockIntegrity: (Block, Double) -> Boolean = { block_, integrity -> block_.integrity > integrity },
    timeoutMs: Long = 20000L,
) {
    val integrity = block.maxIntegrity * percentage / 100.0
    grindUntilIntegrityValue(block, integrity, toolLocation, checkBlockIntegrity, timeoutMs)
}

suspend fun ContextControllerWrapper.grindDownToPercentage(percentage: Double) {
    grindDownToPercentage(context.lastNewBlock!!, percentage)
}

suspend fun ContextControllerWrapper.torchUpToPercentage(percentage: Double) {
    torchUpToPercentage(context.lastNewBlock!!, percentage, torchLocation = context.torchLocation!!)
}

suspend fun SpaceEngineers.grindDownToPercentage(
    block: Block,
    percentage: Double,
    timeoutMs: Long = 20000L,
    grinderLocation: ToolbarLocation
) {
    grindUntilIntegrityPercentage(
        block = block,
        percentage = percentage,
        toolLocation = grinderLocation,
        checkBlockIntegrity = { block_, integrity -> block_.integrity > integrity },
        timeoutMs = timeoutMs
    )
}

suspend fun SpaceEngineers.torchUpToPercentage(block: Block, percentage: Double, torchLocation: ToolbarLocation) {
    grindUntilIntegrityPercentage(
        block = block,
        percentage = percentage,
        toolLocation = torchLocation,
        checkBlockIntegrity = { block_, integrity -> block_.integrity < integrity }
    )
}

fun SpaceEngineers.removeAllBlocks() {
    while (observer.observeBlocks().allBlocks.isNotEmpty()) {
        observer.observeBlocks().allBlocks.forEach {
            try {
                admin.blocks.remove(it.id)
            } catch (e: Exception) {
            }
        }
    }
}
