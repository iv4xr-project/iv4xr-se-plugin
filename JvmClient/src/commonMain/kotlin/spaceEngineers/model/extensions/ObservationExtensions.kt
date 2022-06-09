package spaceEngineers.model.extensions

import spaceEngineers.controller.Observer
import spaceEngineers.model.Block
import spaceEngineers.model.BlockId
import spaceEngineers.model.CubeGrid
import spaceEngineers.model.Observation

val Observation.allBlocks: List<Block>
    get() {
        return this.grids.flatMap { it.blocks }
    }

fun Observation.blockById(blockId: BlockId): Block {
    return allBlocks.first { it.id == blockId }
}

fun Observation.heaviestGrid(): CubeGrid {
    return grids.maxByOrNull { grid -> grid.mass } ?: error("No grid found!")
}

fun Observer.blockById(blockId: BlockId): Block {
    return observeBlocks().blockById(blockId)
}
