package spaceEngineers.model

val SeObservation.allBlocks: List<SeBlock>
    get() {
        return this.grids.flatMap { it.blocks }
    }

data class SeObservation(
    var agentID: String,
    var position: Vec3,
    var orientationForward: Vec3,
    var orientationUp: Vec3,
    var velocity: Vec3,
    var entities: List<BaseSeEntity> = emptyList(),
    var grids: List<SeGrid> = emptyList()
)