package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionQueueItem(
    @SerialName("DisplayName")
    val displayName: String,
    @SerialName("Prerequisites")
    val prerequisites: List<AmountedDefinitionId>,
    @SerialName("Results")
    val results: List<AmountedDefinitionId>,
)


@Serializable
data class AmountedDefinitionId(
    @SerialName("Id")
    val id: DefinitionId,
    @SerialName("Amount")
    val amount: Int,
) {
    override fun toString(): String {
        return "${amount}x $id"
    }
}

@Serializable
data class TerminalInventoryData(
    @SerialName("LeftInventories")
    val leftInventories: List<Inventory>,
    @SerialName("RightInventories")
    val rightInventories: List<Inventory>,
)


@Serializable
data class TerminalProductionData(
    @SerialName("ProductionQueue")
    val productionQueue: List<ProductionQueueItem>,
    @SerialName("Inventory")
    val inventory: List<AmountedDefinitionId>,
    @SerialName("Blueprints")
    val blueprints: List<ProductionQueueItem>,
    @SerialName("ProductionCooperativeMode")
    val productionCooperativeMode: Boolean,
    @SerialName("ProductionRepeatMode")
    val productionRepeatMode: Boolean,
)

@Serializable
data class TerminalScreenData(
    @SerialName("SelectedTab")
    val selectedTab: String,

    @SerialName("Production")
    val production: TerminalProductionData,
    @SerialName("Inventory")
    val inventory: TerminalInventoryData,
)
