package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataBlock(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3,
    @SerialName("Size")
    override val size: Vec3,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
) : Block
