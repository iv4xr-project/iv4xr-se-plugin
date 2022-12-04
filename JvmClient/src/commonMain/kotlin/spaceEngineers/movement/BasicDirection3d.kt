package spaceEngineers.movement

import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3F

val leftShift = setOf(16, 160)

enum class BasicDirection3d(
    val vector: Vec3F,
    val keycode: Map<CharacterMovementType, Set<Int>>
) {
    NONE(Vec3F.ZERO, emptyMap()),

    UP(Vec3F.UP, fromChar(32.toChar())),
    DOWN(Vec3F.DOWN, fromChar('c')),
    LEFT(Vec3F.LEFT, fromChar('a')),
    RIGHT(Vec3F.RIGHT, fromChar('d')),
    FORWARD(Vec3F.FORWARD, fromChar('w')),
    BACKWARD(Vec3F.BACKWARD, fromChar('s'));

    companion object {
        fun directionFromString(value: String): BasicDirection3d {
            return when (value) {
                "forward" -> FORWARD
                "backward" -> BACKWARD
                "left" -> LEFT
                "right" -> RIGHT
                "up" -> UP
                "down" -> DOWN
                else -> {
                    error("No basic direction defined for value $value")
                }
            }
        }

        fun excludingNone(): List<BasicDirection3d> {
            return values().filter { it != NONE }
        }
    }
}

fun fromChar(char: Char): Map<CharacterMovementType, Set<Int>> {
    val upper = char.uppercaseChar().code
    return mapOf(
        CharacterMovementType.WALK to setOf(upper),
        CharacterMovementType.RUN to setOf(upper),
        CharacterMovementType.SPRINT to setOf(upper) + leftShift,
    )
}
