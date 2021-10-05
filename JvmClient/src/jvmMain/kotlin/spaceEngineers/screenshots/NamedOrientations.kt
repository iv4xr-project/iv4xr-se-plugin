package spaceEngineers.screenshots

import spaceEngineers.model.Vec3F
import spaceEngineers.model.Vec3F.Companion.FORWARD
import spaceEngineers.model.Vec3F.Companion.UP
import spaceEngineers.model.Vec3F.Companion.LEFT
import spaceEngineers.model.Vec3F.Companion.RIGHT
import spaceEngineers.model.Vec3F.Companion.BACKWARD
import spaceEngineers.model.Vec3F.Companion.DOWN


enum class NamedOrientations(
    val orientationForward: Vec3F,
    val orientationUp: Vec3F,
) {
    FORWARD_UP(FORWARD, UP),
    BACKWARD_UP(BACKWARD, UP),
    LEFT_UP(LEFT, UP),
    RIGHT_UP(RIGHT, UP),
    UP_FORWARD(UP, FORWARD),
    DOWN_BACKWARD(DOWN, BACKWARD),
}
