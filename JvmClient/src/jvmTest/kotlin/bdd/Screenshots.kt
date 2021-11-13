package bdd

import spaceEngineers.model.Block

data class Screenshots(
    val blockType: String,
    val maxIntegrity: Float,
    val screenshots: MutableList<SingleScreenshot> = mutableListOf()
) {
    constructor(block: Block) : this(block.definitionId.type, block.maxIntegrity)
}
