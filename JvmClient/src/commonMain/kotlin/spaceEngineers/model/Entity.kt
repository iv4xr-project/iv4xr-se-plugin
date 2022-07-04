package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias BlockId = String

interface Entity : Pose {
    @SerialName("Id")
    val id: String
}

interface ExtendedEntity : Entity {
    @SerialName("Id")
    override val id: String

    @SerialName("Velocity")
    val velocity: Vec3F

    @SerialName("Name")
    val name: String

    @SerialName("DisplayName")
    val displayName: String

    @SerialName("InScene")
    val inScene: Boolean

    @SerialName("DefinitionId")
    val definitionId: DefinitionId
}


@Serializable
data class BaseEntity(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("Velocity")
    override val velocity: Vec3F,
    @SerialName("Name")
    override val name: String,
    @SerialName("DisplayName")
    override val displayName: String,
    @SerialName("InScene")
    override val inScene: Boolean,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
) : ExtendedEntity
