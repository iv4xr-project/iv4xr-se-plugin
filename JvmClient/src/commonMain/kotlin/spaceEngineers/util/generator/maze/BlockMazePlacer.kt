package spaceEngineers.util.generator.maze

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.LARGE_BLOCK_CUBE_SIDE_SIZE
import spaceEngineers.model.Vec3F


class BlockMazePlacer(
    val maze: Maze,
    val spaceEngineers: SpaceEngineers,
    val bottomBlockDefinitionId: DefinitionId,
    val wallBlockDefinitionId: DefinitionId,
) {

    fun generate() {
        val z = 0
        for (x in 0..maze.width) {
            for (y in 0..maze.height) {
                val backgroundPosition = Vec3F(x, y, z) * LARGE_BLOCK_CUBE_SIDE_SIZE

                spaceEngineers.admin.blocks.placeAt(
                    blockDefinitionId = bottomBlockDefinitionId,
                    position = backgroundPosition,
                    orientationForward = Vec3F.FORWARD,
                    orientationUp = Vec3F.UP,
                )

                val cell = maze[x, y]
                if (!cell.canMove) {
                    repeat(1) {
                        val wallPosition = Vec3F(x, y, 1 + it) * LARGE_BLOCK_CUBE_SIDE_SIZE
                        spaceEngineers.admin.blocks.placeAt(
                            blockDefinitionId = wallBlockDefinitionId,
                            position = wallPosition,
                            orientationForward = Vec3F.FORWARD,
                            orientationUp = Vec3F.UP,
                        )

                    }
                }
            }
        }
    }

}
