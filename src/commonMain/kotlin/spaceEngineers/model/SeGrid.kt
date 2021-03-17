package spaceEngineers.model


data class SeGrid(
    override val id: String,
    override val position: Vec3,
    val blocks: List<SeBlock> = emptyList()
) : SeEntity {

}