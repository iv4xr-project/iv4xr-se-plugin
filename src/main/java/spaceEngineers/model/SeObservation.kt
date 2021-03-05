package spaceEngineers.model


class SeObservation {
    var agentID: String? = null
    var position: Vec3? = null
    var orientationForward: Vec3? = null
    var orientationUp: Vec3? = null
    var velocity: Vec3? = null
    var entities: List<BaseSeEntity> = emptyList()
    var grids: List<SeGrid> = emptyList()
}