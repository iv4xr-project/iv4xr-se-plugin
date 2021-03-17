package bdd

data class SpaceEngineersTestContext(
    var grinderLocation: ToolbarLocation? = null,
    var torchLocation: ToolbarLocation? = null,
    var lastNewBlockId: String? = null,
    val blockTypeToToolbarLocation: MutableMap<String, ToolbarLocation> = mutableMapOf()
) {

    fun updateToolbarLocation(dataTable: List<Map<String, String>>) {
        blockTypeToToolbarLocation.clear()
        blockTypeToToolbarLocation.putAll(
            dataTable.map {
                it["blockType"]!! to ToolbarLocation(slot = it["slot"]!!.toInt(), page = it["page"]!!.toInt())
            }.toMap()
        )
    }

    fun blockToolbarLocation(blockType: String): ToolbarLocation {
        return blockTypeToToolbarLocation[blockType] ?: error("Toolbar location not set for type $blockType")
    }
}