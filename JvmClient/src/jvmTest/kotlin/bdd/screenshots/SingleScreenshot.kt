package bdd.screenshots

import spaceEngineers.model.Block

data class SingleScreenshot(
    val filename: String,
    val integrity: Float,
    val buildRatioUpperBound: Float
) {
    constructor(block: Block, buildRatioUpperBound: Float) : this(
        filename = "${block.definitionId.type}_${block.integrity}.png",
        integrity = block.integrity,
        buildRatioUpperBound = buildRatioUpperBound
    )
}
