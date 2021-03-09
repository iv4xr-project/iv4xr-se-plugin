package spaceEngineers.model


class SeGrid(
    override val id: String,
    override val position: Vec3
) : SeEntity {
    val blocks: List<SeBlock> = emptyList()
}