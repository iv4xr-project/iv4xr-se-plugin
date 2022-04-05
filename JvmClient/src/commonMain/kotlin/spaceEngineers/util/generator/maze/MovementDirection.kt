package spaceEngineers.util.generator.maze

enum class MovementDirection {
    FORWARD, LEFT, RIGHT, BACK;

    val arrow: String
        get() = when (this) {
            BACK -> "▼"
            FORWARD -> "▲"
            LEFT -> "◀"
            RIGHT -> "▶"
        }
}
