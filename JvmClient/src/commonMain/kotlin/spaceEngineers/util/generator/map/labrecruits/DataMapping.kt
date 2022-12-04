package spaceEngineers.util.generator.map.labrecruits

import spaceEngineers.model.Vec3I
import spaceEngineers.util.generator.maze.Direction

val directionToChar = mapOf(
    Direction.UP to 'n',
    Direction.DOWN to 's',
    Direction.LEFT to 'w',
    Direction.RIGHT to 'e',
)

val charToDirection = directionToChar.map {
    it.value to it.key
}.toMap()

fun Direction.toChar(): Char {
    return directionToChar[this] ?: error("No char for direction $this")
}

fun Char.toDirection(): Direction {
    return charToDirection[this] ?: error("No direction for char '$this'")
}

fun Direction.toVec3I(): Vec3I {
    return when (this) {
        Direction.RIGHT -> Vec3I.RIGHT
        Direction.LEFT -> Vec3I.RIGHT
        Direction.UP -> Vec3I.FORWARD
        Direction.DOWN -> Vec3I.BACKWARD
    }
}
