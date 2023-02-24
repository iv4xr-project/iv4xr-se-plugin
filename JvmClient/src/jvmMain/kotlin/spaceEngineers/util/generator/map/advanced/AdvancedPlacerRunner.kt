package spaceEngineers.util.generator.map.advanced

import spaceEngineers.controller.SpaceEngineersJavaProxyBuilder
import spaceEngineers.controller.extend
import spaceEngineers.controller.extensions.removeAllBlocks
import spaceEngineers.model.Color
import spaceEngineers.model.LARGE_BLOCK_CUBE_SIDE_SIZE
import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3I
import spaceEngineers.model.typing.DefinitionIds
import spaceEngineers.util.generator.map.Orientations
import spaceEngineers.util.generator.maze.Maze

fun main() {
    val batch = SpaceEngineersJavaProxyBuilder().localhost()
    val se = batch.extend()
    se.removeAllBlocks()
    val size = 50
    val maze = Maze(height = size, width = size)
    val f = MutableCell.WALL.copy(color = Color.BLUE)
    val ceiling = MutableCell.WALL.copy(color = null)
    val w = MutableCell.WALL.copy()
    val light = MutableCell.WALL.copy(
        id = DefinitionIds.InteriorLight.SmallLight,
        priority = 5,
        orientations = listOf(
            Orientations(forward = Vec3I.DOWN, up = Vec3I.RIGHT),
            Orientations(forward = Vec3I.DOWN, up = Vec3I.LEFT),
            Orientations(forward = Vec3I.DOWN, up = Vec3I.FORWARD),
            Orientations(forward = Vec3I.DOWN, up = Vec3I.BACKWARD),
        )
    )

    val map = mapBuilder {
        cube(start = Vec3I.ZERO, end = Vec3I(size - 1, 1, size - 1), cell = w)
        removeCube(start = Vec3I.ONE.copy(y = 0), end = Vec3I(size - 2, 5, size - 2))
        floor(1).fill(f)
        with(floor(2)) {
            maze.asSequence().forEach { (position, cell) ->
                if (cell.canMove) {
                    remove(position.x, position.y)
                } else {
                    add(position.x, position.y, w)
                }
                if (cell == Maze.Cell.START) {
                    floor(1)[position.x, position.y]?.color = Color.RED
                    remove(position.x, position.y - 1)
                }

                if (cell == Maze.Cell.END) {
                    floor(1)[position.x, position.y]?.color = Color.GREEN
                }
            }
        }
        floor(3).fill(ceiling)
        floor(2).fillMissing(light)
        add(Vec3I.ZERO, w.copy(DefinitionIds.Reactor.LargeBlockSmallGenerator, priority = 10))
        add(Vec3I(0, 1, 0), w.copy(DefinitionIds.GravityGenerator.EMPTY, priority = 12))
    }

    se.admin.character.teleport(
        position = Vec3F(LARGE_BLOCK_CUBE_SIDE_SIZE * 2, LARGE_BLOCK_CUBE_SIDE_SIZE * 2, -10f),
        orientationUp = Vec3F.UP,
        orientationForward = Vec3F.RIGHT
    )
    val advancedPlacer = AdvancedPlacer(se = se, map = map, start = Vec3I.ZERO)
    advancedPlacer.place()
}
