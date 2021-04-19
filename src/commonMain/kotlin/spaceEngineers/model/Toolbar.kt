package spaceEngineers.model

data class Toolbar(
    val items: List<ToolbarItem?>,
    val slotCount: Int,
    val pageCount: Int
) {

    operator fun get(location: ToolbarLocation): ToolbarItem? {
        return items[location.slot + location.page * slotCount];
    }
}