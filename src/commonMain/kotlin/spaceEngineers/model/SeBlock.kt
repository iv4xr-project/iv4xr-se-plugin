package spaceEngineers.model


class SeBlock(
    override val id: String,
    override val position: Vec3,
) : SeEntity {
    var maxIntegrity = 0f
    var buildIntegrity = 0f
    var integrity = 0f
    var blockType: String? = null
    var minPosition: Vec3? = null
    var maxPosition: Vec3? = null
    var size: Vec3? = null
    var orientationForward: Vec3? = null
    var orientationUp: Vec3? = null
}