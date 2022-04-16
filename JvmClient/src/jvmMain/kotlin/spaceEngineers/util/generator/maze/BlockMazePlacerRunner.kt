package spaceEngineers.util.generator.maze

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.extensions.allBlocks
import java.lang.Exception

fun SpaceEngineers.cleanBlocks() {
    observer.observeBlocks().allBlocks.forEach {
        try {
            admin.blocks.remove(it.id)
        } catch (e: Exception) {

        }
    }
}


fun main(args: Array<String>) {
    val spaceEngineers = SpaceEngineersJavaProxyBuilder().localhost()
    spaceEngineers.cleanBlocks()

    val placer = BlockMazePlacer(
        maze = Maze(12, 25),
        spaceEngineers = spaceEngineers,
        bottomBlockDefinitionId = DefinitionId.cubeBlock("LargeBlockArmorBlock"),
        wallBlockDefinitionId = DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock"),
    )
    placer.generateAsSingleGrid()
}
