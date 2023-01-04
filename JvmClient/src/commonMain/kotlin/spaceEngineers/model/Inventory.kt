package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Inventory(
    @SerialName("CurrentMass")
    val currentMass: Float,
    @SerialName("CurrentVolume")
    val currentVolume: Float,
    @SerialName("MaxMass")
    val maxMass: Float,
    @SerialName("MaxVolume")
    val maxVolume: Float,
    @SerialName("CargoPercentage")
    val cargoPercentage: Float,
    @SerialName("Items")
    val items: List<PhysicalObject>,
    @SerialName("Id")
    val id: Int,
)

interface PhysicalObject {
    @SerialName("Id")
    val id: DefinitionId

    @SerialName("Amount")
    val amount: Int

    @SerialName("ItemId")
    val itemId: Int

    @SerialName("Scale")
    val scale: Float
}

@Serializable
data class DataPhysicalObject(
    @SerialName("Id")
    override val id: DefinitionId,
    @SerialName("Amount")
    override val amount: Int,
    @SerialName("ItemId")
    override val itemId: Int,
    @SerialName("Scale")
    override val scale: Float = 0f,
) : PhysicalObject

@Serializable
data class GasContainerObject(
    @SerialName("Id")
    override val id: DefinitionId,
    @SerialName("Amount")
    override val amount: Int,
    @SerialName("ItemId")
    override val itemId: Int,
    @SerialName("Scale")
    override val scale: Float = 0f,
    @SerialName("GasLevel")
    val gasLevel: Float = 0f,
) : PhysicalObject
