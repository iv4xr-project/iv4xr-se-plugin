package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionQueueItem(
    @SerialName("Amount")
    val amount: Int,
    @SerialName("Blueprint")
    val blueprint: BlueprintDefinition,
)

@Serializable
data class BlueprintDefinition(
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

interface BlockOrGroupItem {
    val visible: Boolean
    val text: String
}

@Serializable
data class BlockItem(
    @SerialName("Visible")
    override val visible: Boolean,
    @SerialName("Text")
    override val text: String,
    @SerialName("Block")
    val block: Block,
) : BlockOrGroupItem

@Serializable
data class BlockGroupItem(
    @SerialName("Visible")
    override val visible: Boolean,
    @SerialName("Text")
    override val text: String,
    @SerialName("Name")
    val name: String,
    @SerialName("Blocks")
    val blocks: List<Block>,
) : BlockOrGroupItem


@Serializable
data class TerminalControlPanelData(
    @SerialName("Search")
    val search: String,
    @SerialName("NewGroupName")
    val newGroupName: String,
    @SerialName("GridBlocks")
    val gridBlocks: List<BlockOrGroupItem>,
    @SerialName("ToggleBlock")
    val toggleBlock: Boolean,
    @SerialName("ShowBlockInTerminal")
    val showBlockInTerminal: Boolean,
    @SerialName("ShowBLockInToolbarConfig")
    val showBLockInToolbarConfig: Boolean,
    @SerialName("ShowOnHUD")
    val showOnHUD: Boolean,
    @SerialName("Owner")
    val owner: String,
    @SerialName("TransferTo")
    val transferTo: List<String>,
    @SerialName("ShareBlock")
    val shareBlock: List<String>,
    @SerialName("ShareBlockSelectedIndex")
    val shareBlockSelectedIndex: Int,
)

@Serializable
data class TerminalInventoryData(
    @SerialName("LeftInventories")
    val leftInventories: List<Inventory>,
    @SerialName("RightInventories")
    val rightInventories: List<Inventory>,
)

@Serializable
data class TerminalInfoData(
    @SerialName("GridInfo")
    val gridInfo: String,
    @SerialName("GridName")
    val gridName: String,
    @SerialName("ShowCenterOfMass")
    val showCenterOfMass: Boolean,
    @SerialName("ShowGravityRange")
    val showGravityRange: Boolean,
    @SerialName("ShowSensorsFieldRange")
    val showSensorsFieldRange: Boolean,
    @SerialName("ShowAntennaRange")
    val showAntennaRange: Boolean,
    @SerialName("ShowGridPivot")
    val showGridPivot: Boolean,
)


@Serializable
data class TerminalProductionData(
    @SerialName("ProductionQueue")
    val productionQueue: List<ProductionQueueItem>,
    @SerialName("Inventory")
    val inventory: List<AmountedDefinitionId>,
    @SerialName("Blueprints")
    val blueprints: List<BlueprintDefinition>,
    @SerialName("ProductionCooperativeMode")
    val productionCooperativeMode: Boolean,
    @SerialName("ProductionRepeatMode")
    val productionRepeatMode: Boolean,
)

@Serializable
data class TerminalScreenData(
    @SerialName("SelectedTab")
    val selectedTab: String,
)
