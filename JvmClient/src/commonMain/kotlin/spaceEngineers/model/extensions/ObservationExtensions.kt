package spaceEngineers.model.extensions

import spaceEngineers.model.Block
import spaceEngineers.model.Observation

val Observation.allBlocks: List<Block>
    get() {
        return this.grids.flatMap { it.blocks }
    }
