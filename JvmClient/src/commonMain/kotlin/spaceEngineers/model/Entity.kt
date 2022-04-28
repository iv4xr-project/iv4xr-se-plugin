package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias BlockId = String

interface Entity : Pose {
    @SerialName("Id")
    val id: String
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
) : Entity
