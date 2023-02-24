package spaceEngineers.util.generator.maze

operator fun Position.plus(direction: Direction): DirectedPosition {
    return DirectedPosition(
        position = Position(this.x + direction.vector.x, this.y + direction.vector.y),
        direction = direction
    )
}

operator fun Position.plus(position: Position): Position {
    return Position(this.x + position.x, this.y + position.y)
}

data class Position(val x: Int, val y: Int) {

    fun down(): DirectedPosition {
        return this + Direction.DOWN
    }

    fun up(): DirectedPosition {
        return this + Direction.UP
    }

    fun left(): DirectedPosition {
        return this + Direction.LEFT
    }

    fun right(): DirectedPosition {
        return this + Direction.RIGHT
    }

    fun surround(): Array<DirectedPosition> {
        return arrayOf(
            up(),
            down(),
            left(),
            right()
        )
    }
}
