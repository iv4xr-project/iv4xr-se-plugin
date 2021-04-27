package spaceEngineers.model

val Observation.allBlocks: List<SlimBlock>
    get() {
        return this.grids.flatMap { it.blocks }
    }

data class Observation(
    var agentID: String,
    var position: Vec3,
    var orientationForward: Vec3,
    var orientationUp: Vec3,
    var velocity: Vec3,
    var entities: List<BaseEntity> = emptyList(),
    var grids: List<CubeGrid> = emptyList()
)
