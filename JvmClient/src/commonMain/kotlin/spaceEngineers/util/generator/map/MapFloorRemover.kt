package spaceEngineers.util.generator.map

import spaceEngineers.util.generator.map.labrecruits.*
import spaceEngineers.util.generator.maze.Direction
import spaceEngineers.util.generator.maze.Position
import spaceEngineers.util.generator.maze.plus

enum class ExplorationStates {
    UNEXPLORED, WALL, EXPLORED_USED, EXPLORED_USELESS;

    companion object {
        fun fromCell(cell: LabRecruitCell?): ExplorationStates {
            return when (cell) {
                is Wall -> WALL
                is Door, is Button, is Agent -> EXPLORED_USED
                is UnnecessaryFloor -> EXPLORED_USELESS
                null, is Floor -> UNEXPLORED
                else -> error("no state for $this")
            }
        }
    }
}

enum class SurfaceType {
    UNKNOWN, USEFUL
}

class Surface(
    val initialPosition: Position,
    var type: SurfaceType = SurfaceType.UNKNOWN,
) {
    val unexploredPositions = mutableSetOf<Position>(initialPosition)
    val exploredPositions = mutableSetOf<Position>()

    fun positionsFound(positions: Set<Position>) {
        unexploredPositions.addAll(positions.subtract(exploredPositions))
    }

    fun positionExplored(position: Position) {
        unexploredPositions.remove(position)
        exploredPositions.add(position)
    }
}

data class StatedPosition(val position: Position, val state: ExplorationStates)

class UselessFloorRemover(
    val cells: Array<Array<LabRecruitCell?>>,
    val cellMaker: (Int, Int) -> LabRecruitCell? = { _, _ -> UnnecessaryFloor }
) {
    val surfacesByLowestPosition: MutableMap<Position, MutableSet<Position>> = mutableMapOf()

    val array = Array(cells.width) { x ->
        Array(cells.height) { y ->
            val cell = cells[x][y]
            ExplorationStates.fromCell(cell)
        }
    }

    fun explore() {
        var position: Position? = findFirstOfType(ExplorationStates.UNEXPLORED)
        val surfaces = mutableSetOf<Surface>()
        while (position != null) {
            val surface = Surface(initialPosition = position)
            explore(surface)
            markSurfacePositions(surface)
            surfaces.add(surface)
            position = findFirstOfType(ExplorationStates.UNEXPLORED)
        }
    }

    private fun markSurfacePositions(surface: Surface) {
        if (surface.type == SurfaceType.USEFUL) {
            surface.exploredPositions.forEach {
                array[it.x][it.y] = ExplorationStates.EXPLORED_USED
            }
            return
        }
        surface.exploredPositions.forEach {
            array[it.x][it.y] = ExplorationStates.EXPLORED_USELESS
            cells[it.x][it.y] = cellMaker(it.x, it.y)
        }
    }

    private fun explore(surface: Surface) {
        while (surface.unexploredPositions.isNotEmpty()) {
            val position = surface.unexploredPositions.first()
            val neighbours = nonWallNeighbours(position)
            if (neighbours.any { it.state == ExplorationStates.EXPLORED_USED }) {
                surface.type = SurfaceType.USEFUL
            }
            surface.positionExplored(position)
            surface.positionsFound(neighbours.map { it.position }.toSet())
        }
    }

    private fun nonWallNeighbours(position: Position): Set<StatedPosition> {
        return listOf(
            position + Direction.UP,
            position + Direction.DOWN,
            position + Direction.LEFT,
            position + Direction.RIGHT,
        ).mapNotNull { directedPosition ->
            getAt(directedPosition.position)?.let {
                StatedPosition(directedPosition.position, it)
            }
        }.filter { it.state != ExplorationStates.WALL }.toSet()
    }

    private fun getAt(position: Position): ExplorationStates? {
        if (position.x >= array.width || position.y >= array.height || position.x < 0 || position.y < 0) {
            return null
        }
        return array[position.x][position.y]
    }

    private fun findFirstOfType(states: ExplorationStates): Position? {
        for (y in 0 until array.height) {
            for (x in 0 until array.width) {
                if (array[x][y] == states) {
                    return Position(x, y)
                }
            }
        }
        return null
    }
}

fun Array<Array<LabRecruitCell?>>.removeUselessFloors(): Array<Array<LabRecruitCell?>> {
    UselessFloorRemover(this).explore()
    return this
}

