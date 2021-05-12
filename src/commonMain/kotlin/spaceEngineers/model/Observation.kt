package spaceEngineers.model

val Observation.allBlocks: List<Block>
    get() {
        return this.grids.flatMap { it.blocks }
    }

data class Observation(
    override val id: String,
    override val position: Vec3,
    override val orientationForward: Vec3,
    override val orientationUp: Vec3,
    val velocity: Vec3,
    val grids: List<CubeGrid> = emptyList()
) : Entity
