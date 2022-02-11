package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionQueueItem(
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
data class TerminalScreenData(
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
    @SerialName("SelectedTab")
    val selectedTab: String,
)
