package spaceEngineers.util.generator.map.advanced

import spaceEngineers.model.Vec3I

class FloorBuilder(
    val mapBuilder: MapBuilder,
    val y: Int,
) {

    private fun <R> action(x: Int, zRange: Iterable<Int>, callback: (Int, Int) -> R) =
        action(setOf(x), zRange, callback)

    private fun <R> action(xRange: Iterable<Int>, z: Int, callback: (Int, Int) -> R) =
        action(xRange, setOf(z), callback)

    private fun <R> action(xRange: Iterable<Int>, zRange: Iterable<Int>, callback: (Int, Int) -> R) {
        xRange.forEach { x ->
            zRange.forEach { z ->
                callback(x, z)
            }
        }
    }

    fun add(x: Int, z: Int, cell: MutableCell) {
        mapBuilder.add(
            Vec3I(x, y, z),
            cell,
        )
    }

    fun remove(x: Int, z: Int) {
        mapBuilder.remove(Vec3I(x, y, z))
    }

    fun remove(xRange: IntRange, zRange: IntRange) {
        action(xRange, zRange) { x, z ->
            remove(x, z)
        }
    }

    fun add(xRange: IntRange, zRange: IntRange, cell: MutableCell) {
        action(xRange, zRange) { x, z ->
            add(x, z, cell)
        }
    }

    fun add(x: Int, zRange: Iterable<Int>, cell: MutableCell) {
        action(x, zRange) { x, z ->
            add(x, z, cell)
        }
    }

    fun remove(x: IntRange, z: Int) {
        x.forEach {
            remove(it, z)
        }
    }

    fun fill(cell: MutableCell): FloorBuilder {
        getBoundaries().allBetween().forEach {
            mapBuilder.add(it, cell)
        }
        return this
    }

    fun fillMissing(cell: MutableCell): FloorBuilder {
        getBoundaries().allBetween().forEach {
            if (!mapBuilder.contains(it)) {
                mapBuilder.add(it, cell)
            }
        }
        return this
    }

    fun surround(cell: MutableCell, boundaries: Boundaries = getBoundaries()) {
        boundaries.allBetween()
            .filter { it.x == boundaries.min.x || it.x == boundaries.max.x || it.z == boundaries.min.z || it.x == boundaries.max.z }
            .forEach {
                mapBuilder.add(it, cell)
            }
    }

    fun getBoundaries(): Boundaries {
        return mapBuilder.getBoundaries().toFloor(y)
    }

    fun position(x: Int, z: Int): Vec3I {
        return Vec3I(x = x, y = y, z = z)
    }

    operator fun get(x: Int, z: Int): MutableCell? {
        return mapBuilder[position(x, z)]
    }
}
