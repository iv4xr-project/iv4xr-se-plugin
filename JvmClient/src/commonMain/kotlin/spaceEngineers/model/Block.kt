package spaceEngineers.model


data class Block(
    override val id: String,
    override val position: Vec3,
    override val orientationForward: Vec3,
    override val orientationUp: Vec3,
    val blockType: String,
    val maxIntegrity: Float = 0f,
    val buildIntegrity: Float = 0f,
    val integrity: Float = 0f,
    var minPosition: Vec3,
    var maxPosition: Vec3,
    var size: Vec3,
) : Entity
