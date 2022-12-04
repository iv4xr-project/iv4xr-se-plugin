package spaceEngineers.util.generator.maze

enum class Direction(val vector: Position) {
    UP(Position(0, -1)),
    RIGHT(Position(1, 0)),
    DOWN(Position(0, 1)),
    LEFT(Position(-1, 0));

    val oposite: Direction
        get() = -this

    operator fun unaryMinus(): Direction {
        return when (this) {
            DOWN -> UP
            UP -> DOWN
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }

    fun rotateRight(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun toMovementDirection(): MovementDirection {
        return when (this) {
            UP -> MovementDirection.FORWARD
            RIGHT -> MovementDirection.RIGHT
            DOWN -> MovementDirection.BACK
            LEFT -> MovementDirection.LEFT
        }
    }

    fun toMovementDirection(direction: Direction): MovementDirection {
        return when (this) {
            UP ->
                direction.toMovementDirection()
            LEFT ->
                direction.rotateRight().toMovementDirection()
            DOWN ->
                direction.rotateRight().rotateRight().toMovementDirection()
            RIGHT ->
                direction.rotateRight().rotateRight().rotateRight().toMovementDirection()
        }
    }

    val arrow: String
        get() = when (this) {
            DOWN -> "▼"
            UP -> "▲"
            LEFT -> "◀"
            RIGHT -> "▶"
        }
}
