package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataBlock(
    @SerialName("Id")
    override val id: BlockId,
    @SerialName("Position")
    override val position: Vec3F,
    @SerialName("OrientationForward")
    override val orientationForward: Vec3F,
    @SerialName("OrientationUp")
    override val orientationUp: Vec3F,
    @SerialName("DefinitionId")
    override val definitionId: DefinitionId,
    @SerialName("MaxIntegrity")
    override val maxIntegrity: Float = 0f,
    @SerialName("BuildIntegrity")
    override val buildIntegrity: Float = 0f,
    @SerialName("Integrity")
    override val integrity: Float = 0f,
    @SerialName("MinPosition")
    override val minPosition: Vec3F,
    @SerialName("MaxPosition")
    override val maxPosition: Vec3F,
    @SerialName("GridPosition")
    override val gridPosition: Vec3I,
    @SerialName("Size")
    override val size: Vec3F,
    @SerialName("UseObjects")
    override val useObjects: List<UseObject> = emptyList(),
    @SerialName("Functional")
    override val functional: Boolean = false,
    @SerialName("Working")
    override val working: Boolean = false,
    @SerialName("OwnerId")
    override val ownerId: CharacterId,
) : Block
