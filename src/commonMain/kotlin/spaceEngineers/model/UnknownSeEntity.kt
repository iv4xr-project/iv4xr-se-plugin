package spaceEngineers.model


class BaseSeEntity(
    override val id: String,
    override val position: Vec3
) : SeEntity {

}