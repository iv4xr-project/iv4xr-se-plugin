package spaceEngineers.model.extensions

import spaceEngineers.controller.Observer
import spaceEngineers.model.Block
import spaceEngineers.model.Observation

val Observation.allBlocks: List<Block>
    get() {
        return this.grids.flatMap { it.blocks }
    }

fun Observation.blockById(blockId: String): Block {
    return allBlocks.first { it.id == blockId }
}

fun Observer.blockById(blockId: String): Block {
    return observeBlocks().blockById(blockId)
}
