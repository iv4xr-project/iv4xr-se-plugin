package spaceEngineers.model

data class ToolbarLocation(
    val slot: Int,
    val page: Int
) {
    companion object {
        fun fromIndex(index: Int, pageSize: Int = 10): ToolbarLocation {
            return ToolbarLocation(
                slot = index % pageSize,
                page = index / pageSize
            )
        }
    }
}
