package spaceEngineers.model

data class Toolbar(
    val items: List<ToolbarItem?>,
    val slotCount: Int,
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
