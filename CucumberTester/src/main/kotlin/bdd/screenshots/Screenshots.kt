package bdd.screenshots

import kotlinx.serialization.Serializable
import spaceEngineers.model.Block

@Serializable
data class Screenshots(
    val blockType: String,
    val maxIntegrity: Float,
    val screenshots: MutableList<SingleScreenshot> = mutableListOf()
) {
    constructor(block: Block) : this(block.definitionId.type, block.maxIntegrity)
}
