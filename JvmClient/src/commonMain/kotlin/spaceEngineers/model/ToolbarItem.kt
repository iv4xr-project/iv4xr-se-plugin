package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface ToolbarItem {
    val name: String
    val enabled: Boolean
}

@Serializable
data class DataToolbarItem(
    @SerialName("Name")
    override val name: String,
    @SerialName("Enabled")
    override val enabled: Boolean
) : ToolbarItem

@Serializable
data class DataToolbarItemActions(
    @SerialName("ActionId")
    val actionId: String,
    @SerialName("Name")
    override val name: String,
    @SerialName("Enabled")
    override val enabled: Boolean
) : ToolbarItem

@Serializable
data class DataToolbarItemTerminalBlock(
    @SerialName("BlockEntityId")
    val blockEntityId: Long,
    @SerialName("ActionId")
    val actionId: String,
    @SerialName("Name")
    override val name: String,
    @SerialName("Enabled")
    override val enabled: Boolean
) : ToolbarItem

@Serializable
data class DataToolbarItemDefinition(
    @SerialName("Id")
    val id: DefinitionId,
    @SerialName("Name")
    override val name: String,
    @SerialName("Enabled")
    override val enabled: Boolean
) : ToolbarItem {

    override fun toString(): String {
        return "$id - $name"
    }
}
