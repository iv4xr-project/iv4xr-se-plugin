package spaceEngineers.util.generator.maze

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.util.generator.map.BlockPlacementInformation
import spaceEngineers.util.generator.map.DataBlockPlacementInformation
import spaceEngineers.util.generator.map.MapPlacer

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

    val placer = MapPlacer(
        map = MazeMapLayer(
            Maze(12, 25),
            DataBlockPlacementInformation(
                DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock"), Vec3I.FORWARD, Vec3I.LEFT
            ),
        ),
        spaceEngineers = spaceEngineers,
    )
    placer.generate()
}
