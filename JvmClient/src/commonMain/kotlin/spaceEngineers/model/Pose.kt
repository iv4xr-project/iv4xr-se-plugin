package spaceEngineers.model

fun Pose.rotationMatrix(): RotationMatrix {
    return RotationMatrix.fromForwardAndUp(orientationForward, orientationUp)
}

interface Pose {
    val position: Vec3
    val orientationForward: Vec3
    val orientationUp: Vec3
}
