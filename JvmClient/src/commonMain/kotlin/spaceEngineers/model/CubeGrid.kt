package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CubeGrid(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("Blocks")
    val blocks: List<Block> = emptyList(),
    @SerialName("Mass")
    val mass: Float,
    @SerialName("Parked")
    val parked: Boolean = false,
    @SerialName("Velocity")
    override val velocity: Vec3F,
    @SerialName("Name")
    override val name: String,
    @SerialName("DisplayName")
    override val displayName: String,
    @SerialName("InScene")
    override val inScene: Boolean,
) : ExtendedEntity
