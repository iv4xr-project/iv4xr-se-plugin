package spaceEngineers.model

data class BasePose(
    override val position: Vec3,
    override val orientationForward: Vec3,
    override val orientationUp: Vec3
) : Pose
