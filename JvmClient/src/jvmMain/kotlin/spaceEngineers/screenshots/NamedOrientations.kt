package spaceEngineers.screenshots

import spaceEngineers.model.Vec3
import spaceEngineers.model.Vec3.Companion.FORWARD
import spaceEngineers.model.Vec3.Companion.UP
import spaceEngineers.model.Vec3.Companion.LEFT
import spaceEngineers.model.Vec3.Companion.RIGHT
import spaceEngineers.model.Vec3.Companion.BACKWARD
import spaceEngineers.model.Vec3.Companion.DOWN


enum class NamedOrientations(
    val orientationForward: Vec3,
    val orientationUp: Vec3,
) {
    FORWARD_UP(FORWARD, UP),
    BACKWARD_UP(BACKWARD, UP),
    LEFT_UP(LEFT, UP),
    RIGHT_UP(RIGHT, UP),
    UP_FORWARD(UP, FORWARD),
    DOWN_BACKWARD(DOWN, BACKWARD),
}
