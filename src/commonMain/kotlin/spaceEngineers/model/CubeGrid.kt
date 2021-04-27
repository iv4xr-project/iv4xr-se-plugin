package spaceEngineers.model


data class CubeGrid(
    override val id: String,
    override val position: Vec3,
    val blocks: List<SlimBlock> = emptyList()
) : Entity {

}
