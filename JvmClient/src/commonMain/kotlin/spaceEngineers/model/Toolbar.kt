package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import spaceEngineers.controller.extensions.toNullIfMinusOne

@Serializable
data class Toolbar(
    @SerialName("Items")
    val items: List<ToolbarItem?>,
    @SerialName("SlotCount")
    val slotCount: Int,
    @SerialName("PageCount")
    val pageCount: Int
) {

    operator fun get(location: ToolbarLocation): ToolbarItem? {
        return items[location.slot + location.page * slotCount];
    }

    fun findLocation(blockType: String): ToolbarLocation? {
        return items.indexOfFirst {
            it is DataToolbarItemDefinition? &&
            it?.id?.type == blockType
        }.toNullIfMinusOne()?.let {
            ToolbarLocation.fromIndex(it)
        }
    }

    fun findLocation(definitionId: DefinitionId): ToolbarLocation? {
        return items.indexOfFirst {
            it is DataToolbarItemDefinition? &&
            it?.id == definitionId
        }.toNullIfMinusOne()?.let {
            ToolbarLocation.fromIndex(it)
        }
    }

}
