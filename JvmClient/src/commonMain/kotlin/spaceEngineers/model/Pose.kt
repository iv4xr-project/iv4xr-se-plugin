package spaceEngineers.model

interface Pose {
    val position: Vec3
    val orientationForward: Vec3
    val orientationUp: Vec3
}
