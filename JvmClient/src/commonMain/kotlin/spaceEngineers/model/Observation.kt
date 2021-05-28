package spaceEngineers.model

data class Observation(
    override val id: String,
    override val position: Vec3,
    override val orientationForward: Vec3,
    override val orientationUp: Vec3,
    val velocity: Vec3,
    val grids: List<CubeGrid> = emptyList(),
    val extent: Vec3,
    val jetpackRunning: Boolean,
    val camera: BasePose,
    val headLocalXAngle: Float,
    val headLocalYAngle: Float,
    val targetBlock: Block?,
) : Entity
