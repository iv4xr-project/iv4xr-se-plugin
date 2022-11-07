package spaceEngineers.util.generator.maze

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
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

    val blockPlacementInformation = DataBlockPlacementInformation(
        blockId = DefinitionId.cubeBlock("LargeHeavyBlockArmorBlock"),
        color = Vec3F(0.5, 1.0, 1.0)
    )


    val placer = MapPlacer(
        map = MazeMapLayer(
            maze = Maze(12, 25),
            blockToPlace = blockPlacementInformation,
        ),
        spaceEngineers = spaceEngineers,
        floorPlacer = blockPlacementInformation,
    )
    placer.generate()
}
