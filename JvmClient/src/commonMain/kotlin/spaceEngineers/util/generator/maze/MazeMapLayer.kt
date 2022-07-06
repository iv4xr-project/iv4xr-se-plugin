package spaceEngineers.util.generator.maze

import spaceEngineers.util.generator.map.BlockPlacementInformation
import spaceEngineers.util.generator.map.MapLayer

class MazeMapLayer(val maze: Maze, val blockToPlace: BlockPlacementInformation) : MapLayer {
    override val width: Int
        get() = maze.width
    override val height: Int
        get() = maze.height

    private fun Maze.Cell.toBlockPlacementInformation(): BlockPlacementInformation? {
        return if (canMove) {
            null
        } else {
            blockToPlace
        }
    }

    override fun get(x: Int, y: Int): BlockPlacementInformation? {
        return maze[x, y].toBlockPlacementInformation()
    }
}
