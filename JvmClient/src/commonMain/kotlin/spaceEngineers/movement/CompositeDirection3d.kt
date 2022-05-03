package spaceEngineers.movement

import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.sum

fun CompositeDirection3d.toKeyboardSnapshot(movementType: CharacterMovementType): KeyboardSnapshot {
    return KeyboardSnapshot(
        pressedKeys = basicDirections.flatMap { it.keycode[movementType] ?: emptySet() },
        text = emptyList()
    )
}


enum class CompositeDirection3d(val basicDirections: Set<BasicDirection3d>) {
    NONE(BasicDirection3d.NONE),

    UP(BasicDirection3d.UP),
    DOWN(BasicDirection3d.DOWN),
    LEFT(BasicDirection3d.LEFT),
    RIGHT(BasicDirection3d.RIGHT),
    FORWARD(BasicDirection3d.FORWARD),
    BACKWARD(BasicDirection3d.BACKWARD),

    UP_LEFT(BasicDirection3d.UP, BasicDirection3d.LEFT),
    UP_LEFT_FORWARD(BasicDirection3d.UP, BasicDirection3d.LEFT, BasicDirection3d.FORWARD),
    UP_FORWARD(BasicDirection3d.UP, BasicDirection3d.FORWARD),
    UP_RIGHT_FORWARD(BasicDirection3d.UP, BasicDirection3d.RIGHT, BasicDirection3d.FORWARD),
    UP_RIGHT(BasicDirection3d.UP, BasicDirection3d.RIGHT),
    UP_RIGHT_BACKWARD(BasicDirection3d.UP, BasicDirection3d.RIGHT, BasicDirection3d.BACKWARD),
    UP_BACKWARD(BasicDirection3d.UP, BasicDirection3d.BACKWARD),
    UP_LEFT_BACKWARD(BasicDirection3d.UP, BasicDirection3d.LEFT, BasicDirection3d.BACKWARD),

    LEFT_FORWARD(BasicDirection3d.LEFT, BasicDirection3d.FORWARD),
    RIGHT_FORWARD(BasicDirection3d.RIGHT, BasicDirection3d.FORWARD),
    RIGHT_BACKWARD(BasicDirection3d.RIGHT, BasicDirection3d.BACKWARD),
    LEFT_BACKWARD(BasicDirection3d.LEFT, BasicDirection3d.BACKWARD),

    DOWN_LEFT(BasicDirection3d.DOWN, BasicDirection3d.LEFT),
    DOWN_LEFT_FORWARD(BasicDirection3d.DOWN, BasicDirection3d.LEFT, BasicDirection3d.FORWARD),
    DOWN_FORWARD(BasicDirection3d.DOWN, BasicDirection3d.FORWARD),
    DOWN_RIGHT_FORWARD(BasicDirection3d.DOWN, BasicDirection3d.RIGHT, BasicDirection3d.FORWARD),
    DOWN_RIGHT(BasicDirection3d.DOWN, BasicDirection3d.RIGHT),
    DOWN_RIGHT_BACKWARD(BasicDirection3d.DOWN, BasicDirection3d.RIGHT, BasicDirection3d.BACKWARD),
    DOWN_BACKWARD(BasicDirection3d.DOWN, BasicDirection3d.BACKWARD),
    DOWN_LEFT_BACKWARD(BasicDirection3d.DOWN, BasicDirection3d.LEFT, BasicDirection3d.BACKWARD),
    ;

    constructor(vararg basicDirection3d: BasicDirection3d) : this(basicDirection3d.toSet())

    val vector: Vec3F = basicDirections.map { it.vector }.sum()
}
