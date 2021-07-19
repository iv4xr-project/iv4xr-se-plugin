package spaceEngineers.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        val itemIndex = items.indexOfFirst {
            it?.subType == blockType
        }
        if (itemIndex < 0) {
            return null
        }
        return ToolbarLocation.fromIndex(itemIndex)
    }
}
