package spaceEngineers.model.extensions

import spaceEngineers.controller.Observer
import spaceEngineers.model.*

fun Observation.blocksByCustomName(customName: String): List<TerminalBlock> {
    return allBlocks.filterIsInstance<TerminalBlock>().filter { it.customName == customName }
}

fun Observation.blockByCustomName(customName: String): TerminalBlock {
    val blocks = allBlocks
    return blocks.filterIsInstance<TerminalBlock>().firstOrNull { it.customName == customName } ?: error(
            "Block with name $customName not found, found only ${
                blocks.filterIsInstance<spaceEngineers.model.TerminalBlock>().map { it.customName }
            }")
}

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
