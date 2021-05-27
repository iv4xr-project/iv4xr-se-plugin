package spaceEngineers.model.extensions

import spaceEngineers.model.Pose
import spaceEngineers.model.RotationMatrix

fun Pose.rotationMatrix(): RotationMatrix {
    return RotationMatrix.fromForwardAndUp(orientationForward, orientationUp)
}
