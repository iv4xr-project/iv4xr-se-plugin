package spaceEngineers.util.generator.maze

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.LARGE_BLOCK_CUBE_SIDE_SIZE
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I


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

    fun generateAsSingleGrid() {
        val z = 0
        var gridId: String? = null
        for (x in 0..maze.width) {
            for (y in 0..maze.height) {
                if (gridId == null) {
                    spaceEngineers.admin.blocks.placeAt(
                        blockDefinitionId = bottomBlockDefinitionId,
                        position = Vec3F(x, y, z) * LARGE_BLOCK_CUBE_SIDE_SIZE,
                        orientationForward = Vec3F.FORWARD,
                        orientationUp = Vec3F.UP,
                    )
                    gridId = spaceEngineers.observer.observeBlocks().grids.first().id
                } else {
                    spaceEngineers.admin.blocks.placeInGrid(
                        gridId = gridId,
                        blockDefinitionId = bottomBlockDefinitionId,
                        orientationUp = Vec3I.UP,
                        orientationForward = Vec3I.FORWARD,
                        minPosition = Vec3I(x, y, z)
                    )
                }

                val cell = maze[x, y]
                if (!cell.canMove) {
                    repeat(1) {
                        spaceEngineers.admin.blocks.placeInGrid(
                            gridId = gridId,
                            blockDefinitionId = wallBlockDefinitionId,
                            orientationUp = Vec3I.UP,
                            orientationForward = Vec3I.FORWARD,
                            minPosition = Vec3I(x, y, it + 1)
                        )
                    }
                }
            }
        }
    }

}
