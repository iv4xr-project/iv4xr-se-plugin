package spaceEngineers.model


data class CubeGrid(
    override val id: String,
    override val position: Vec3,
    override val orientationForward: Vec3,
    override val orientationUp: Vec3,
    val blocks: List<Block> = emptyList(),
) : Entity
