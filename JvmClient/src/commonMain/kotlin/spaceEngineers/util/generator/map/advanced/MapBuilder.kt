package spaceEngineers.util.generator.map.advanced

import spaceEngineers.model.Vec3I
import spaceEngineers.model.extensions.allBetween

fun mapBuilder(block: MapBuilder.() -> Unit): Map<Vec3I, MutableCell> {
    val builder = MapBuilder()
    block(builder)
    return builder.build()
}

class MapBuilder(
    private val cells: MutableMap<Vec3I, MutableCell> = mutableMapOf(),
) {

    fun offset(offset: Vec3I): MapBuilder {
        val newMap = cells.map { (position, cell) ->
            position + offset to cell.copy()
        }
        cells.clear()
        cells.putAll(newMap)
        return this
    }

    fun cube(start: Vec3I, end: Vec3I, cell: MutableCell): MapBuilder {
        val positions = start.allBetween(end).asIterable()
        return batchPositions(positions, cell)
    }

    fun removeCube(start: Vec3I, end: Vec3I): MapBuilder {
        removePositions(start.allBetween(end).asIterable())
        return this
    }

    private fun removePositions(positions: Iterable<Vec3I>): MapBuilder {
        positions.forEach { vec ->
            cells.remove(vec)
        }
        return this
    }

    private fun batchPositions(positions: Iterable<Vec3I>, cell: MutableCell): MapBuilder {
        positions.forEach { position ->
            cells[position] = cell.copy(priority = cell.priority, customName = cell.customName)
        }
        return this
    }

    fun floor(y: Int): FloorBuilder {
        return FloorBuilder(mapBuilder = this, y = y)
    }

    fun add(position: Vec3I, cell: MutableCell) {
        cells[position] = cell.copy(priority = cell.priority, customName = cell.customName)
    }

    fun build(): Map<Vec3I, MutableCell> {
        return cells
    }

    fun getBoundaries(positions: Set<Vec3I> = cells.keys): Boundaries {
        require(positions.isNotEmpty()) { "Map is empty." }
        val minX = positions.minBy { it.x }.x
        val minY = positions.minBy { it.y }.y
        val minZ = positions.minBy { it.z }.z
        val maxX = positions.maxBy { it.x }.x
        val maxY = positions.maxBy { it.y }.y
        val maxZ = positions.maxBy { it.z }.z
        val minPosition = Vec3I(minX, minY, minZ)
        val maxPosition = Vec3I(maxX, maxY, maxZ)
        return Boundaries(minPosition, maxPosition)
    }

    fun getBoundaries(y: Int): Boundaries {
        return getBoundaries(cells.keys.filter { it.y == y }.toSet())
    }

    fun remove(position: Vec3I) {
        cells.remove(position)
    }

    operator fun get(position: Vec3I): MutableCell? {
        return cells[position]
    }

    fun contains(position: Vec3I): Boolean {
        return cells.contains(position)
    }
}
