package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Block(
    @SerialName("Id")
    override val id: String,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("BlockType")
    val blockType: String,
    @SerialName("MaxIntegrity")
    val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    val integrity: Float = 0f,
    @SerialName("MinPosition")
    var minPosition: Vec3,
    @SerialName("MaxPosition")
    var maxPosition: Vec3,
    @SerialName("Size")
    var size: Vec3,
    @SerialName("UseObjects")
    val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    val functional: Boolean = false,
) : Entity
