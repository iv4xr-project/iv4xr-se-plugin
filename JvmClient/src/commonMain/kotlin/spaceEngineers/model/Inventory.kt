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
    val items: List<InventoryItem>,
)

@Serializable
data class InventoryItem(
    @SerialName("Id")
    val id: DefinitionId,
    @SerialName("Amount")
    val amount: Int,
)
