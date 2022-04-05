package spaceEngineers.util.generator.maze

data class DirectedPosition(val position: Position, val direction: Direction) {
    val back: DirectedPosition
        get() = position + direction.oposite

    fun id(prefix: String = "BLUDISTE"): String {
        return "u_${prefix}_${position.x}_${position.y}_${direction.name}"
    }
}
