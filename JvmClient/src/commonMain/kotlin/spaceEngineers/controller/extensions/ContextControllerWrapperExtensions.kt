package spaceEngineers.controller.extensions

import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.model.Block

suspend fun ContextControllerWrapper.grindDownToPercentage(block: Block, percentage: Double) {
    grindDownToPercentage(
        block = block,
        percentage = percentage,
        grinderLocation = context.grinderLocation ?: error("grinder location not set")
    )
}

suspend fun ContextControllerWrapper.torchBackToMax(block: Block) {
    torchBackToPercentage(block, 100.0)
}

suspend fun ContextControllerWrapper.torchBackToPercentage(block: Block, percentage: Double) {
    torchUpToPercentage(
        block = block,
        percentage = percentage,
        torchLocation = context.torchLocation ?: error("torch location not set")
    )
}
